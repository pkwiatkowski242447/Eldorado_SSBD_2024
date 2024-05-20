package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule.ScheduleBadPropertiesException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.ScheduleServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.log.ScheduleLogMessages;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Service managing execution of scheduled tasks.
 * Configuration concerning tasks is set in application.properties.
 */
@Slf4j
@Service
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ScheduleService implements ScheduleServiceInterface {

    /**
     * AccountMOKFacade used for operations on account entities.
     */
    private final AccountMOKFacade accountMOKFacade;

    /**
     * TokenFacade used for operations on then accounts.
     */
    private final TokenFacade tokenFacade;

    /**
     * MailProvider used for sending emails.
     */
    private final MailProvider mailProvider;

    /**
     * String value that specifies time after which deletion will occur.
     * Deletion time is specified by <code>scheduler.not_verified_account_delete_time</code> property.
     */
    @Value("${scheduler.not_verified_account_delete_time}")
    private String deleteTime;

    /**
     * String value that specifies time after which blocked accounts will be unblocked.
     * Unblock time is specified by <code>scheduler.blocked_account_unblock_time</code> property/
     */
    @Value("${scheduler.blocked_account_unblock_time}")
    private String unblockTime;
    @Value("${mail.account.creation.confirmation.url}")
    private String accountCreationConfirmationUrl;
    @Value("${account.resend.creation.confirmation.after.hours}")
    private int resendRegistrationConfirmationEmailAfterHours;

    /**
     * Autowired constructor for the service.
     *
     * @param accountMOKFacade Facade used for managing user accounts.
     * @param tokenFacade      Facade used for managing tokens used for many account related activities.
     */
    @Autowired
    public ScheduleService(AccountMOKFacade accountMOKFacade, TokenFacade tokenFacade, MailProvider mailProvider) {
        this.accountMOKFacade = accountMOKFacade;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
    }

    /**
     * Removes Accounts which have not finished registration.
     * Time for the Account verification is set by <code>scheduler.not_verified_account_delete_time</code> property.
     *
     * @throws ScheduleBadPropertiesException Threw when problem with properties occurs.
     */
    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    public void deleteNotVerifiedAccount() throws ScheduleBadPropertiesException {
        log.info(ScheduleLogMessages.INVOKING_DELETE_ACCOUNTS_MESS);

        // Find not verified accounts
        List<Account> inactiveAccounts;
        try {
            inactiveAccounts =
                    accountMOKFacade.findAllAccountsMarkedForDeletion(Long.parseLong(deleteTime), TimeUnit.HOURS);
        } catch (NumberFormatException e) {
            log.error(ScheduleLogMessages.BAD_PROP_FORMAT.formatted("not_verified_account_delete_time"));
            throw new ScheduleBadPropertiesException(ScheduleLogMessages.BAD_PROP_FORMAT.formatted("not_verified_account_delete_time"));
        }

        if (inactiveAccounts.isEmpty()) {
            log.info(ScheduleLogMessages.NO_ACCOUNTS_TO_DELETE_MESS);
            return;
        }

        log.info(ScheduleLogMessages.LIST_ACCOUNTS_TO_DELETE_IDS, inactiveAccounts.stream().map(Account::getId).toList());

        // Delete accounts and linked tokens
        inactiveAccounts.forEach(a -> {
            tokenFacade.removeByAccount(a.getId());
            accountMOKFacade.remove(a);
        });
    }

    /**
     * This method will be invoked every hour in order to check if half the time to active registered account has passed.
     * If so then new registration token will be generated, and new message for activating user account will be sent to specified e-mail address.
     */
    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    public void resendConfirmationEmail() {
        log.info(ScheduleLogMessages.INVOKING_RESEND_CONFIRMATION_EMAIL);

        // Find all tokens of type REGISTER
        List<Token> registerTokens = tokenFacade.findByTokenType(Token.TokenType.REGISTER);

        registerTokens.forEach(token -> {
            Account account = token.getAccount();
            if (account.getCreationDate().isBefore(LocalDateTime.now().minusHours(resendRegistrationConfirmationEmailAfterHours))) {
                String confirmationURL = accountCreationConfirmationUrl + token.getTokenValue();

                mailProvider.sendRegistrationConfirmEmail(account.getName(),
                        account.getLastname(),
                        account.getEmail(),
                        confirmationURL,
                        account.getAccountLanguage());
                tokenFacade.removeByTypeAndAccount(Token.TokenType.REGISTER, account.getId());
            }
        });
    }

    /**
     * Unblock Accounts which have been blocked by login incorrectly certain amount of time.
     * Time for the Account blockade is set by <code>scheduler.blocked_account_unblock_time</code> property.
     *
     * @throws ScheduleBadPropertiesException Threw when problem with properties occurs.
     */
    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    public void unblockAccount() throws ScheduleBadPropertiesException {
        log.info(ScheduleLogMessages.INVOKING_UNBLOCK_ACCOUNTS_MESS);

        // Find blocked accounts
        List<Account> blockedAccounts;
        try {
            blockedAccounts = accountMOKFacade
                    .findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes(Long.parseLong(unblockTime), TimeUnit.HOURS);
        } catch (NumberFormatException e) {
            log.error(ScheduleLogMessages.BAD_PROP_FORMAT.formatted("scheduler.blocked_account_unblock_time"));
            throw new ScheduleBadPropertiesException(ScheduleLogMessages.BAD_PROP_FORMAT.formatted("scheduler.blocked_account_unblock_time"));
        }

        if (blockedAccounts.isEmpty()) {
            log.info(ScheduleLogMessages.NO_ACCOUNTS_TO_UNBLOCK_MESS);
            return;
        }

        log.info(ScheduleLogMessages.LIST_ACCOUNTS_TO_UNBLOCK_IDS, blockedAccounts.stream().map(Account::getId).toList());

        // Unblock accounts
        blockedAccounts.forEach((account -> {
            account.unblockAccount();
            try {
                accountMOKFacade.edit(account);
            } catch (ApplicationBaseException exception) {
                log.error("Exception of type: {} was throw while activating user: {} after it was blocked for {} hours.",
                        exception.getClass().getSimpleName(), account.getLogin(), this.deleteTime);
            }

            // Send notification mail
            mailProvider.sendUnblockAccountInfoEmail(account.getName(),
                    account.getLastname(),
                    account.getEmail(),
                    account.getAccountLanguage());
        }));
    }
}
