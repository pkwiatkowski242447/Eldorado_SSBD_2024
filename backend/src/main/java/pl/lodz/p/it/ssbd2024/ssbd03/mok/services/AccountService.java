package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
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
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountEmailChangeException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountSameEmailException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountValidationException;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages.SAME_EMAIL_EXCEPTION;
import static pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages.VALIDATION_EXCEPTION;

/**
 * Service managing Accounts.
 *
 * @see Account
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class AccountService {

    private final AccountMOKFacade accountFacade;
    private final PasswordEncoder passwordEncoder;
    private final TokenFacade tokenFacade;
    private final MailProvider mailProvider;
    private final JWTProvider jwtProvider;
    private final Validator validator;

    /**
     * Autowired constructor for the service.
     *
     * @param accountFacade     Facade responsible for users accounts management.
     * @param passwordEncoder   This component is used to generate hashed passwords for user accounts (not to store them in their original form).
     * @param tokenFacade       This facade is responsible for manipulating tokens, used for various, user account related operations.
     * @param mailProvider      This component is used to send e-mail messages to e-mail address of users (where message depends on their actions).
     * @param jwtProvider       This component is used to generate token values for token facade.
     */
    @Autowired
    public AccountService(AccountMOKFacade accountFacade,
                          PasswordEncoder passwordEncoder,
                          TokenFacade tokenFacade,
                          MailProvider mailProvider,
                          JWTProvider jwtProvider,
                          Validator validator) {
        this.accountFacade = accountFacade;
        this.passwordEncoder = passwordEncoder;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
        this.jwtProvider = jwtProvider;
        this.validator = validator;
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
        mailProvider.sendBlockAccountInfoEmail(account.getName(), account.getLastname(),
                account.getEmail(), account.getAccountLanguage());
        ///TODO handle exception for trying to block an account by more than 1 admin???
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

            String tokenValue = jwtProvider.generateActionToken(newStaffAccount, 12);
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

            String tokenValue = jwtProvider.generateActionToken(newAdminAccount, 12);
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
     * Activate account with a token from activation URL, sent to user e-mail address, specified during registration.
     *
     * @param token Last part of the activation URL sent in a message to users e-mail address.
     *
     * @return Boolean value indicating whether activation of the account was successful or not.
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
     * Confirm e-mail change with a token from confirmation URL, sent to the new e-mail address.
     *
     * @param token Last part of the confirmation URL sent in a message to user's e-mail address.
     *
     * @return Returns true if the e-mail confirmation was successful. Returns false if the token is expired or invalid.
     * @throws AccountNotFoundException Threw if the account connected to the token does not exist.
     */
    public boolean confirmEmail(String token) throws AccountNotFoundException {
        Token tokenFromDB = tokenFacade.findByTokenValue(token).orElse(null);
        Optional<Account> accountFromDB = accountFacade.find(jwtProvider.extractAccountId(token));
        Account account = accountFromDB.orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));
        if (tokenFromDB == null || !jwtProvider.isTokenValid(token, account)) {
            return false;
        } else {
            account.setVerified(true);
            accountFacade.edit(account);
            tokenFacade.remove(tokenFromDB);
            return true;
        }
    }

    /**
     * Retrieve Account that match the parameters, in a given order.
     *
     * @param login         Account's login. A phrase is sought in the logins.
     * @param firstName     Account's owner first name. A phrase is sought in the names.
     * @param lastName      Account's owner last name. A phrase is sought in the last names.
     * @param order         Ordering in which user accounts should be returned.
     * @param pageNumber    Number of the page with searched users accounts.
     * @param pageSize      Number of the users accounts per page.
     *
     * @return List of user accounts that match the given parameters.
     */
    public List<Account> getAccountsByMatchingLoginFirstNameAndLastName(String login,
                                                                        String firstName,
                                                                        String lastName,
                                                                        boolean order,
                                                                        int pageNumber,
                                                                        int pageSize) {
        return accountFacade.findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination(login, firstName, lastName, order, pageNumber, pageSize);
    }

    /**
     * Retrieve all accounts in the system.
     *
     * @param pageNumber    The page number of the results to return.
     * @param pageSize      The number of results to return per page.
     *
     * @return              A list of all accounts in the system, ordered by account login, with pagination applied.
     */
    public List<Account> getAllAccounts(int pageNumber, int pageSize) {
        return accountFacade.findAllAccountsWithPagination(pageNumber, pageSize);
    }

    /**
     * Retrieves an Account by the login.
     *
     * @param login Login of the searched user account.
     *
     * @return If Account with the given login was found returns Account, otherwise returns null.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Account getAccountByLogin(String login) {
        Optional<Account> account = accountFacade.findByLoginAndRefresh(login);
        return account.orElse(null);
    }

    /**
     * Retrieves from the database account by id.
     *
     * @param id Account's id.
     * @return Returns Optional containing the requested account if found, otherwise returns empty Optional.
     */
    public Optional<Account> getAccountById(UUID id) {
        return accountFacade.findAndRefresh(id);
    }

    /**
     * Changes the e-mail of the specified Account.
     *
     * @param account  Account which the e-mail will be changed.
     * @param newEmail New e-mail address.
     * @throws AccountEmailChangeException Threw if any problem related to the e-mail occurs.
     *                                     Contains a key to an internationalized message.
     *                                     Additionally, if the problem was caused by an incorrect new mail,
     *                                     the cause is set to <code>AccountValidationException</code> which contains more details about the incorrect fields.
     */
    public void changeEmail(Account account, String newEmail) throws AccountEmailChangeException {
        try {
            if (account.getEmail().equals(newEmail)) throw new AccountSameEmailException(SAME_EMAIL_EXCEPTION);

            account.setEmail(newEmail);
            account.setVerified(false);
            var errors = validator.validateObject(account);
            if (errors.hasErrors()) throw new AccountValidationException(VALIDATION_EXCEPTION,
                    errors.getFieldErrors()
                            .stream()
                            .map(v -> new AccountValidationException.FieldConstraintViolation(v.getField(),
                                    Optional.ofNullable(v.getRejectedValue()).orElse(" ").toString(),
                                    Optional.ofNullable(v.getDefaultMessage()).orElse(" ")))
                            .collect(Collectors.toSet()));
            accountFacade.edit(account);
        } catch (AccountValidationException e) {
            //TODO internationalization
            log.error(e.getMessage());
            throw new AccountEmailChangeException(I18n.ACCOUNT_CONSTRAINT_VALIDATION_EXCEPTION, e);
        } catch (AccountSameEmailException e) {
            log.error(e.getMessage());
            throw new AccountEmailChangeException(I18n.ACCOUNT_SAME_EMAIL_EXCEPTION, e);
        } catch (ConstraintViolationException e) {
            log.error(e.getMessage());
            throw new AccountEmailChangeException(I18n.ACCOUNT_EMAIL_COLLISION_EXCEPTION, e);
        }
    }
}
