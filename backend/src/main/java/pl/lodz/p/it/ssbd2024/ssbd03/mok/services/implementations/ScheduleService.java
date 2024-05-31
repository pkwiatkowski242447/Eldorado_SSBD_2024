package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.util.RunAsSystem;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AccountHistoryData;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.OperationType;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountHistoryDataFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.ScheduleServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.TokenProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Service managing execution of scheduled tasks.
 * Configuration concerning tasks is set in consts.properties.
 */
@Slf4j
@Service
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ScheduleService implements ScheduleServiceInterface {

    /**
     * AccountMOKFacade used for operations on account entities.
     */
    private final AccountMOKFacade accountMOKFacade;
    private final AccountHistoryDataFacade historyDataFacade;

    /**
     * TokenFacade used for operations on then accounts.
     */
    private final TokenFacade tokenFacade;

    /**
     * MailProvider used for sending emails.
     */
    private final MailProvider mailProvider;

    /**
     * TokenProvider component used for automatic generation of action tokens.
     */
    private final TokenProvider tokenProvider;

    /**
     * String value that specifies time after which deletion will occur.
     * Deletion time is specified by <code>scheduler.not_active_account_delete_time</code> property.
     */
    @Value("${scheduler.not_active_account_delete_time}")
    private String deleteTime;

    /**
     * String value that specifies time after which blocked accounts will be unblocked.
     * Unblock time is specified by <code>scheduler.blocked_account_unblock_time</code> property.
     */
    @Value("${scheduler.blocked_account_unblock_time}")
    private String unblockTime;

    /**
     * String value representing activation URL sent in the activation e-mail message
     * used to activate newly created user account.
     */
    @Value("${mail.account.creation.confirmation.url}")
    private String accountCreationConfirmationUrl;

    /**
     * Integer value representing number of hours, which the activation e-mail
     * message should be sent after.
     */
    @Value("${account.resend.creation.confirmation.after.hours}")
    private int resendRegistrationConfirmationEmailAfterHours;

    /**
     * String value that specifies the number of days after which the account will be blocked.
     * Number of days is specified by <code>scheduler.max_days_without_authentication</code> property.
     */
    @Value("${scheduler.max_days_without_authentication}")
    private String maxDaysWithoutAuthentication;

    /**
     * Autowired constructor for the service.
     *
     * @param accountMOKFacade  Facade used for managing user accounts.
     * @param historyDataFacade Facade used for inserting information about account modifications to the database.
     * @param tokenFacade       Facade used for managing tokens used for many account related activities.
     * @param tokenProvider     Component used for automatic generation of action tokens.
     * @param mailProvider      Component used for sending e-mail messages to e-mail addresses connected to certain
     *                          user accounts.
     */
    @Autowired
    public ScheduleService(AccountMOKFacade accountMOKFacade,
                           AccountHistoryDataFacade historyDataFacade,
                           TokenFacade tokenFacade,
                           MailProvider mailProvider,
                           TokenProvider tokenProvider) {
        this.accountMOKFacade = accountMOKFacade;
        this.historyDataFacade = historyDataFacade;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
        this.tokenProvider = tokenProvider;
    }

    @RunAsSystem
    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    @RolesAllowed({Authorities.REMOVE_ACCOUNT})
    public void deleteNotActivatedAccounts() {
        log.info("Method: deleteNotActivatedAccount(), used for removing not activated accounts, was invoked.");

        List<Account> inactiveAccounts = new ArrayList<>();
        try {
            inactiveAccounts = accountMOKFacade.findAllAccountsMarkedForDeletion(Long.parseLong(deleteTime), TimeUnit.HOURS);
        } catch (NumberFormatException | ApplicationBaseException exception) {
            log.error("Exception: {} occurred while searching for accounts to be removed. Cause: {}.",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }

        if (inactiveAccounts.isEmpty()) {
            log.info("No accounts to be removed were found.");
            return;
        }

        log.info("List of identifiers of accounts to be removed: {}", inactiveAccounts.stream().map(Account::getId).toList());

        for (Account account : inactiveAccounts) {
            try {
                tokenFacade.removeByAccount(account.getId());
                accountMOKFacade.remove(account);

                mailProvider.sendRemoveAccountInfoEmail(account.getName(), account.getLastname(), account.getEmail(), account.getAccountLanguage());
            } catch (ApplicationBaseException exception) {
                log.error("Exception: {} occurred while removing account with id: {}. Cause: {}.",
                        exception.getClass().getSimpleName(), account.getId(), exception.getMessage());
            }
        }
    }

    @RunAsSystem
    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    @RolesAllowed({Authorities.RESEND_EMAIL_CONFIRMATION_MAIL})
    public void resendConfirmationEmail() {
        log.info("Method: resendConfirmationEmail(), used for sending account activation message, was invoked.");

        List<Token> registerTokens = new ArrayList<>();
        try {
            registerTokens = tokenFacade.findByTokenType(Token.TokenType.REGISTER);
        } catch (ApplicationBaseException exception) {
            log.error("Exception: {} occurred while searching for account needing activation. Cause: {}.",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }

        if (registerTokens.isEmpty()) {
            log.info("There are no activation e-mail messages to be sent right now.");
            return;
        }

        for (Token token : registerTokens) {
            try {
                Account account = token.getAccount();
                if (account.getCreationTime().isBefore(LocalDateTime.now().minusHours(resendRegistrationConfirmationEmailAfterHours))) {
                    tokenFacade.removeByTypeAndAccount(Token.TokenType.REGISTER, account.getId());
                    Token tokenObject = tokenProvider.generateAccountActivationToken(account);
                    tokenFacade.create(tokenObject);

                    String confirmationURL = accountCreationConfirmationUrl + tokenObject.getTokenValue();

                    mailProvider.sendRegistrationConfirmEmail(account.getName(),
                            account.getLastname(),
                            account.getEmail(),
                            confirmationURL,
                            account.getAccountLanguage());
                }
            } catch (ApplicationBaseException exception) {
                log.error("Exception: {} occurred while swapping activation token for account with id: {}. Cause: {}.",
                        exception.getClass().getSimpleName(), token.getAccount().getId(), exception.getMessage());
            }
        }
    }

    @RunAsSystem
    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    @RolesAllowed({Authorities.UNBLOCK_ACCOUNT})
    public void unblockAccount() {
        log.info("Method: unblockAccount(), used for unblocking accounts blocked by incorrect login attempts, was invoked.");

        List<Account> blockedAccounts = new ArrayList<>();
        try {
            blockedAccounts = accountMOKFacade.findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes(Long.parseLong(unblockTime), TimeUnit.HOURS);
        } catch (NumberFormatException | ApplicationBaseException exception) {
            log.error("Exception: {} occurred while searching for accounts to be unblocked. Cause: {}.",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }

        if (blockedAccounts.isEmpty()) {
            log.info("There are no account to be unblocked right now.");
            return;
        }

        log.info("List of identifiers of accounts to be unblocked: {}", blockedAccounts.stream().map(Account::getId).toList());

        blockedAccounts.forEach((account -> {
            account.unblockAccount();
            try {
                accountMOKFacade.edit(account);
                historyDataFacade.create(new AccountHistoryData(account,
                        OperationType.UNBLOCK,
                        accountMOKFacade.findByLogin(SecurityContextHolder
                                        .getContext()
                                        .getAuthentication()
                                        .getName())
                                .orElse(null)));
            } catch (ApplicationBaseException exception) {
                log.error("Exception of type: {} was throw while activating user: {} after it was blocked for {} hours.",
                        exception.getClass().getSimpleName(), account.getLogin(), this.unblockTime);
            }

            mailProvider.sendUnblockAccountInfoEmail(account.getName(),
                    account.getLastname(),
                    account.getEmail(),
                    account.getAccountLanguage());
        }));
    }

    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    @RolesAllowed({Authorities.BLOCK_ACCOUNT})
    public void suspendAccountWithoutAuthenticationForSpecifiedTime() {
        log.info("Method: suspendAccountWithoutAuthenticationForSpecifiedTime() was invoked.");

        List<Account> accountsToSuspend = new ArrayList<>();
        try {
            accountsToSuspend = accountMOKFacade.findAllAccountsWithoutRecentActivity(LocalDateTime.now().minus(Long.parseLong(maxDaysWithoutAuthentication), TimeUnit.DAYS.toChronoUnit()));
        } catch (NumberFormatException | ApplicationBaseException exception) {
            log.error("Exception: {} occurred while searching for accounts to be suspended. Cause: {}.",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }

        if (accountsToSuspend.isEmpty()) {
            log.info("There are no account to be suspended right now.");
            return;
        }

        log.info("List of identifiers of accounts to be suspended: {}", accountsToSuspend.stream().map(Account::getId).toList());

        accountsToSuspend.forEach((account -> {
            account.setSuspended(true);
            try {
                accountMOKFacade.edit(account);
                historyDataFacade.create(new AccountHistoryData(account,
                        OperationType.SUSPEND,
                        null)
                );
            } catch (ApplicationBaseException exception) {
                log.error("Exception of type: {} was throw while suspending user: {}.",
                        exception.getClass().getSimpleName(), account.getLogin());
            }

            mailProvider.sendSuspendAccountInfoEmail(account.getName(), account.getLastname(), account.getEmail(), account.getAccountLanguage());
        }));
    }
}
