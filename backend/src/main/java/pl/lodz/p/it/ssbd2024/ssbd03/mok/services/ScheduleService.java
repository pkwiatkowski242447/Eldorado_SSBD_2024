package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule.ScheduleBadProperties;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.ScheduleConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ScheduleService {

    private final AccountMOKFacade accountMOKFacade;

    private final TokenFacade tokenFacade;

    private final MailProvider mailProvider;

    @Value("${scheduler.not_verified_account_delete_time}")
    private String deleteTime;

    @Autowired
    public ScheduleService(AccountMOKFacade accountMOKFacade, TokenFacade tokenFacade, MailProvider mailProvider) {
        this.accountMOKFacade = accountMOKFacade;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
    }

    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteNotVerifiedAccount() throws ScheduleBadProperties {
        log.info(ScheduleConsts.INVOKING_DELETE_ACCOUNTS_MESS);

        // Find not verified accounts
        List<Account> inactiveAccounts;
        try {
            inactiveAccounts =
                    accountMOKFacade.findAllAccountsMarkedForDeletion(Long.parseLong(deleteTime), TimeUnit.HOURS);
        } catch (NumberFormatException e) {
            log.error(ScheduleConsts.BAD_PROP_FORMAT.formatted("not_verified_account_delete_time"));
            throw new ScheduleBadProperties(ScheduleConsts.BAD_PROP_FORMAT.formatted("not_verified_account_delete_time"));
        }

        if (inactiveAccounts.isEmpty()) {
            log.info(ScheduleConsts.NO_ACCOUNTS_TO_DELETE_MESS);
            return;
        }

        log.info(ScheduleConsts.LIST_ACCOUNTS_TO_DELETE_IDS, inactiveAccounts.stream().map(Account::getId).toList());

        // Delete accounts and linked tokens
        inactiveAccounts.forEach(a -> {
            tokenFacade.removeByAccount(a.getId());
            accountMOKFacade.remove(a);
        });
    }

    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    @Transactional(propagation = Propagation.REQUIRED)
    public void resendConfirmationEmail() {
        log.info(ScheduleConsts.INVOKING_RESEND_CONFIRMATION_EMAIL);

        // Find all tokens of type REGISTER
        List<Token> registerTokens = tokenFacade.findByTokenType(Token.TokenType.REGISTER);

        registerTokens.forEach(token -> {
            Account account = token.getAccount();
            if (account.getCreationDate().isBefore(LocalDateTime.now().minus(12, ChronoUnit.HOURS))) {
                String confirmationURL = "http://localhost:8080/api/v1/account/activate-account/" + token;
                mailProvider.sendRegistrationConfirmEmail(account.getName(),
                                                          account.getLastname(),
                                                          account.getEmail(),
                                                          confirmationURL,
                                                          account.getAccountLanguage());
                tokenFacade.removeByTypeAndAccount(Token.TokenType.REGISTER, account.getId());
            }
        });
    }
}
