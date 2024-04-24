package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountCreationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

/**
 * Service managing Accounts.
 *
 * @see Account
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AccountService {

    private final AccountMOKFacade accountFacade;
    private final PasswordEncoder passwordEncoder;
    private final TokenFacade tokenFacade;
    private final MailProvider mailProvider;
    private final JWTProvider jwtProvider;

    /**
     * Autowired constructor for the service.
     *
     * @param accountFacade
     * @param passwordEncoder
     * @param tokenFacade
     * @param mailProvider
     * @param jwtProvider
     */
    @Autowired
    public AccountService(AccountMOKFacade accountFacade,
                          PasswordEncoder passwordEncoder,
                          TokenFacade tokenFacade,
                          MailProvider mailProvider,
                          JWTProvider jwtProvider) {
        this.accountFacade = accountFacade;
        this.passwordEncoder = passwordEncoder;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Create new account, which will have default user level of Client.
     *
     * @param login         User login, used in order to authenticate to the application.
     * @param password      User password, used in combination with login to authenticate to the application.
     * @param firstName     First name of the user.
     * @param lastName      Last name of the user.
     * @param email         Email address, which will be used to send messages (e.g. confirmation messages) for actions in the application.
     * @param phoneNumber   Phone number of the user.
     * @param language      Predefined language constant used for internationalizing all messages for user (initially browser value constant but could be set).
     *
     * @return Newly created account, with given data, and default Client user level.
     *
     * @throws AccountCreationException When persisting newly created account with client user level results in Persistence exception.
     */
    public Account registerClient(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws AccountCreationException {
        try {
            Account account = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
            account.setAccountLanguage(language);
            UserLevel clientLevel = new Client();
            clientLevel.setAccount(account);
            account.addUserLevel(clientLevel);

            this.accountFacade.create(account);

            return account;
        } catch (PersistenceException exception) {
            throw new AccountCreationException(exception.getMessage(), exception);
        }
    }

    /**
     * Method for blocking an account by its UUID.
     *
     * @param id Account identifier
     * @throws AccountNotFoundException Threw when there is no account with given login.
     * @throws AccountAlreadyBlockedException Threw when the account is already blocked.
     * @throws IllegalOperationException Threw when user try to block their own account.
     */
    public void blockAccount(UUID id) throws AccountNotFoundException, AccountAlreadyBlockedException, IllegalOperationException {
        Account account = accountFacade.findAndRefresh(id).orElseThrow(AccountNotFoundException::new);
        if (account.getBlocked()) {
            throw new AccountAlreadyBlockedException("This account is already blocked");
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getName().equals(account.getLogin())) {
            log.error("You cannot block your own account!");
            throw new IllegalOperationException("You cannot block your own account!");
        }

        account.setBlocked(true);
        // When admin blocks the account property blockedTime is not set
        accountFacade.edit(account);

        // Sending information email
        mailProvider.sendBlockAccountInfoEmail(account.getName(), account.getLastname(), account.getEmail());
        ///TODO obsluga bledu dla proby zablokowania konta użytkownika przez więcej niż 1 administratora???
    }

    /**
     * This method is used to create new account, which will have default user level of Staff, create
     * appropriate register token, save it to the database, and at the - send the account activation
     * email to the given email address.
     *
     * @param login         User login, used in order to authenticate to the application.
     * @param password      User password, used in combination with login to authenticate to the application.
     * @param firstName     First name of the user.
     * @param lastName      Last name of the user.
     * @param email         Email address, which will be used to send messages (e.g. confirmation messages) for actions in the application.
     * @param phoneNumber   Phone number of the user.
     * @param language      Predefined language constant used for internationalizing all messages for user (initially browser constant value but could be set).
     *
     * @throws AccountCreationException This exception will be thrown if any Persistence exception occurs.
     */
    public void registerStaff(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws AccountCreationException {
        try {
            Account newStaffAccount = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
            newStaffAccount.setAccountLanguage(language);
            UserLevel staffUserLevel = new Staff();
            staffUserLevel.setAccount(newStaffAccount);
            newStaffAccount.addUserLevel(staffUserLevel);

            accountFacade.create(newStaffAccount);

            String tokenValue = jwtProvider.generateRegistrationToken(newStaffAccount);
            tokenFacade.create(new Token(tokenValue, newStaffAccount, Token.TokenType.REGISTER));

            String confirmationURL = "http://localhost:8080/api/v1/account/activate-account/%s".formatted(tokenValue);

            mailProvider.sendRegistrationConfirmEmail(newStaffAccount.getName(),
                    newStaffAccount.getLastname(),
                    newStaffAccount.getEmail(),
                    confirmationURL,
                    newStaffAccount.getAccountLanguage());
        } catch (PersistenceException exception) {
            throw new AccountCreationException(I18n.STAFF_ACCOUNT_CREATION_EXCEPTION);
        }
    }

    /**
     * This method is used to create new account, which will have default user level of Admin, create
     * appropriate register token, save it to the database, and at the - send the account activation
     * email to the given email address.
     *
     * @param login         User login, used in order to authenticate to the application.
     * @param password      User password, used in combination with login to authenticate to the application.
     * @param firstName     First name of the user.
     * @param lastName      Last name of the user.
     * @param email         Email address, which will be used to send messages (e.g. confirmation messages) for actions in the application.
     * @param phoneNumber   Phone number of the user.
     * @param language      Predefined language constant used for internationalizing all messages for user (initially browser constant value but could be set).
     *
     * @throws AccountCreationException This exception will be thrown if any Persistence exception occurs.
     */
    public void registerAdmin(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws AccountCreationException {
        try {
            Account newAdminAccount = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
            newAdminAccount.setAccountLanguage(language);
            UserLevel adminUserLevel = new Admin();
            adminUserLevel.setAccount(newAdminAccount);
            newAdminAccount.addUserLevel(adminUserLevel);

            accountFacade.create(newAdminAccount);

            String tokenValue = jwtProvider.generateRegistrationToken(newAdminAccount);
            tokenFacade.create(new Token(tokenValue, newAdminAccount, Token.TokenType.REGISTER));

            String confirmationURL = "http://localhost:8080/api/v1/account/activate-account/%s".formatted(tokenValue);

            mailProvider.sendRegistrationConfirmEmail(newAdminAccount.getName(),
                    newAdminAccount.getLastname(),
                    newAdminAccount.getEmail(),
                    confirmationURL,
                    newAdminAccount.getAccountLanguage());
        } catch (PersistenceException exception) {
            throw new AccountCreationException(I18n.ADMIN_ACCOUNT_CREATION_EXCEPTION);
        }
    }

    /**
     * Activate account with a token from URL.
     * @param token token to activate account
     * @return
     */
    public boolean activateAccount(String token) {
        Optional<Account> accountFromDB = accountFacade.find(jwtProvider.extractAccountId(token));
        Account account = accountFromDB.orElse(null);
        if (!jwtProvider.isTokenValid(token, account)) {
            return false;
        } else {
            account.setActive(true);
            account.setVerified(true);
            accountFacade.edit(account);
            tokenFacade.findByTokenValue(token).ifPresent(tokenFacade::remove);
            return true;
        }
    }

    /**
     * Retrieve Account that match the parameters.
     *
     * @param login      Account's login. A phrase is sought in the logins.
     * @param firstName  Account's owner first name. A phrase is sought in the names.
     * @param lastName   Account's owner last name. A phrase is sought in the last names.
     * @param order
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List<Account> getAccountsByMatchingLoginFirstNameAndLastName(String login,
                                                                        String firstName,
                                                                        String lastName,
                                                                        boolean order,
                                                                        int pageNumber,
                                                                        int pageSize) {
        return accountFacade.findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination(login, firstName, lastName, order, pageNumber, pageSize);
    }

    public List<Account> getAllAccounts(int pageNumber, int pageSize) {
        return accountFacade.findAllAccountsWithPagination(pageNumber, pageSize);
    }
}
