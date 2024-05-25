package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule.ScheduleBadPropertiesException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.ScheduleServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.log.ScheduleLogMessages;
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
     * TokenProvider component used for automatic generation of action tokens.
     */
    private final TokenProvider tokenProvider;

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
     * @param tokenProvider    Component used for automatic generation of action tokens.
     * @param mailProvider     Component used for sending e-mail messages to e-mail addresses connected to certain
     *                         user accounts.
     */
    @Autowired
    public ScheduleService(AccountMOKFacade accountMOKFacade,
                           TokenFacade tokenFacade,
                           MailProvider mailProvider,
                           TokenProvider tokenProvider) {
        this.accountMOKFacade = accountMOKFacade;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Removes Accounts which have not finished registration.
     * Time for the Account verification is set by <code>scheduler.not_verified_account_delete_time</code> property.
     */
    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    public void deleteNotVerifiedAccount() {
        log.info("Method: deleteNotVerifiedAccount(), used for removing not activated accounts, was invoked.");

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
            } catch (ApplicationBaseException exception) {
                log.error("Exception: {} occurred while removing account with id: {}. Cause: {}.",
                        exception.getClass().getSimpleName(), account.getId(), exception.getMessage());
            }
        }
    }

    /**
     * This method will be invoked every hour in order to check if half the time to active registered account has passed.
     * If so then new registration token will be generated, and new message for activating user account will be sent to specified e-mail address.
     */
    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    public void resendConfirmationEmail() {
        log.info("Method: resendConfirmationEmail(), used for sending account activation message, was invoked.");

        List<Token> registerTokens = new ArrayList<>();
        try {
            registerTokens = tokenFacade.findByTokenType(Token.TokenType.REGISTER);
        } catch (ApplicationBaseException exception) {
            log.error("Exception: {} occurred while searching for account needing activation. Cause: {}.",
                    exception.getClass().getSimpleName(), exception.getMessage());
        }

        for (Token token : registerTokens) {
            try {
                Account account = token.getAccount();
                if (account.getCreationDate().isBefore(LocalDateTime.now().minusHours(resendRegistrationConfirmationEmailAfterHours))) {
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

    /**
     * Unblock Accounts which have been blocked by login incorrectly certain amount of time.
     * Time for the Account blockade is set by <code>scheduler.blocked_account_unblock_time</code> property.
     */
    @Override
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
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
            } catch (ApplicationBaseException exception) {
                log.error("Exception of type: {} was throw while activating user: {} after it was blocked for {} hours.",
                        exception.getClass().getSimpleName(), account.getLogin(), this.deleteTime);
            }

            mailProvider.sendUnblockAccountInfoEmail(account.getName(),
                    account.getLastname(),
                    account.getEmail(),
                    account.getAccountLanguage());
        }));
    }
}
