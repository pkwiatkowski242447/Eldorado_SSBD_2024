package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations;

import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Admin;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Staff;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyUnblockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.old.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AccountServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.TokenServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service managing Accounts.
 *
 * @see Account
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AccountService implements AccountServiceInterface {

    @Value("${mail.account.creation.confirmation.url}")
    private String accountCreationConfirmationUrl;

    @Value("${account.creation.confirmation.period.length.hours}")
    private int accountCreationConfirmationPeriodLengthHours;

    /**
     * AccountFacade used for operations on account entities.
     */
    private final AccountMOKFacade accountFacade;
    /**
     * PasswordEncoder used for encoding passwords.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * TokenFacade used for operations on token entities.
     */
    private final TokenFacade tokenFacade;
    /**
     * Component used to send e-mail messages to user e-mail address (depending on the actions they perform).
     */
    private final MailProvider mailProvider;
    /**
     * TokenProvider used for operations on TOKENS.
     */
    private final JWTProvider jwtProvider;
    /**
     * TokenServiceInterface used for operations on tokens.
     */
    private final TokenServiceInterface tokenService;

    /**
     * Autowired constructor for the service.
     *
     * @param accountFacade   Facade responsible for users accounts management.
     * @param passwordEncoder This component is used to generate hashed passwords for user accounts (not to store them in their original form).
     * @param tokenFacade     This facade is responsible for manipulating tokens, used for various, user account related operations.
     * @param mailProvider    This component is used to send e-mail messages to e-mail address of users (where message depends on their actions).
     * @param jwtProvider     This component is used to generate token values for token facade.
     * @param tokenService    Service used for more complicated token operations.
     */
    @Autowired
    public AccountService(AccountMOKFacade accountFacade,
                          PasswordEncoder passwordEncoder,
                          TokenFacade tokenFacade,
                          MailProvider mailProvider,
                          JWTProvider jwtProvider,
                          TokenServiceInterface tokenService) {
        this.accountFacade = accountFacade;
        this.passwordEncoder = passwordEncoder;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
        this.jwtProvider = jwtProvider;
        this.tokenService = tokenService;
    }

    /**
     * Create new account, which will have default user level of Client.
     *
     * @param login       User login, used in order to authenticate to the application.
     * @param password    User password, used in combination with login to authenticate to the application.
     * @param firstName   First name of the user.
     * @param lastName    Last name of the user.
     * @param email       Email address, which will be used to send messages (e.g. confirmation messages) for actions in the application.
     * @param phoneNumber Phone number of the user.
     * @param language    Predefined language constant used for internationalizing all messages for user (initially browser value constant but could be set).
     * @throws ApplicationBaseException Superclass for all exceptions that could be thrown by the aspect, intercepting facade create method.
     */
    @Override
    public void registerClient(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language)
            throws ApplicationBaseException {
        Account newClientAccount = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
        newClientAccount.setAccountLanguage(language);
        UserLevel clientLevel = new Client();
        clientLevel.setAccount(newClientAccount);
        newClientAccount.addUserLevel(clientLevel);

        this.accountFacade.create(newClientAccount);

        String tokenValue = jwtProvider.generateActionToken(newClientAccount, (this.accountCreationConfirmationPeriodLengthHours / 2) * 60, ChronoUnit.MINUTES);
        tokenFacade.create(new Token(tokenValue, newClientAccount, Token.TokenType.REGISTER));

        String confirmationURL = this.accountCreationConfirmationUrl + tokenValue;

        mailProvider.sendRegistrationConfirmEmail(newClientAccount.getName(),
                newClientAccount.getLastname(),
                newClientAccount.getEmail(),
                confirmationURL,
                newClientAccount.getAccountLanguage());
    }

    /**
     * This method is used to create new account, which will have default user level of Staff, create
     * appropriate register token, save it to the database, and at the - send the account activation
     * email to the given email address.
     *
     * @param login       User login, used in order to authenticate to the application.
     * @param password    User password, used in combination with login to authenticate to the application.
     * @param firstName   First name of the user.
     * @param lastName    Last name of the user.
     * @param email       Email address, which will be used to send messages (e.g. confirmation messages) for actions in the application.
     * @param phoneNumber Phone number of the user.
     * @param language    Predefined language constant used for internationalizing all messages for user (initially browser constant value but could be set).
     * @throws AccountCreationException This exception will be thrown if any Persistence exception occurs.
     */
    @Override
    public void registerStaff(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws AccountCreationException {
        try {
            Account newStaffAccount = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
            newStaffAccount.setAccountLanguage(language);
            UserLevel staffUserLevel = new Staff();
            staffUserLevel.setAccount(newStaffAccount);
            newStaffAccount.addUserLevel(staffUserLevel);

            accountFacade.create(newStaffAccount);

            String tokenValue = jwtProvider.generateActionToken(newStaffAccount, 12, ChronoUnit.HOURS);
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
     * @param login       User login, used in order to authenticate to the application.
     * @param password    User password, used in combination with login to authenticate to the application.
     * @param firstName   First name of the user.
     * @param lastName    Last name of the user.
     * @param email       Email address, which will be used to send messages (e.g. confirmation messages) for actions in the application.
     * @param phoneNumber Phone number of the user.
     * @param language    Predefined language constant used for internationalizing all messages for user (initially browser constant value but could be set).
     * @throws AccountCreationException This exception will be thrown if any Persistence exception occurs.
     */
    @Override
    public void registerAdmin(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws AccountCreationException {
        try {
            Account newAdminAccount = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
            newAdminAccount.setAccountLanguage(language);
            UserLevel adminUserLevel = new Admin();
            adminUserLevel.setAccount(newAdminAccount);
            newAdminAccount.addUserLevel(adminUserLevel);

            accountFacade.create(newAdminAccount);

            String tokenValue = jwtProvider.generateActionToken(newAdminAccount, 12, ChronoUnit.HOURS);
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
     * Method for blocking an account by its UUID.
     *
     * @param id Account identifier
     * @throws AccountNotFoundException       Threw when there is no account with given login.
     * @throws AccountAlreadyBlockedException Threw when the account is already blocked.
     * @throws IllegalOperationException      Threw when user try to block their own account.
     */
    @Override
    public void blockAccount(UUID id) throws AccountNotFoundException, AccountAlreadyBlockedException, IllegalOperationException {
        Account account = accountFacade.findAndRefresh(id).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));
        if (account.getBlocked() && account.getBlockedTime() == null) {
            throw new AccountAlreadyBlockedException();
        }

        account.blockAccount(true);
        accountFacade.edit(account);

        // Sending information email
        mailProvider.sendBlockAccountInfoEmail(account.getName(), account.getLastname(),
                account.getEmail(), account.getAccountLanguage(), true);
        ///TODO handle exception for trying to block an account by more than 1 admin??? - optimistic lock
    }

    /**
     * Method for unblocking an account by its UUID.
     *
     * @param id Account identifier
     * @throws AccountNotFoundException         Threw when there is no account with given login.
     * @throws AccountAlreadyUnblockedException Threw when the account is already unblocked.
     */
    @Override
    public void unblockAccount(UUID id) throws AccountNotFoundException, AccountAlreadyUnblockedException {
        Account account = accountFacade.findAndRefresh(id).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));
        if (!account.getBlocked()) {
            throw new AccountAlreadyUnblockedException();
        }

        account.unblockAccount();
        accountFacade.edit(account);

        // Sending information email
        mailProvider.sendUnblockAccountInfoEmail(account.getName(), account.getLastname(),
                account.getEmail(), account.getAccountLanguage());
        ///TODO handle exception for trying to unblock an account by more than 1 admin??? - optimistic lock
    }

    /**
     * This method is used to modify user personal data.
     *
     * @param modifiedAccount Account with potentially modified properties: name, lastname, phoneNumber.
     * @return Account object with applied modifications
     * @throws AccountNotFoundException Threw if the account with passed login property does not exist.
     */
    @Override
    public Account modifyAccount(Account modifiedAccount) throws AccountNotFoundException {
        //FIXME SecurityContext there or in controller?
        Account foundAccount = accountFacade.findByLogin(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));
        //TODO optimistic lock
        foundAccount.setName(modifiedAccount.getName());
        foundAccount.setLastname(modifiedAccount.getLastname());
        foundAccount.setPhoneNumber(modifiedAccount.getPhoneNumber());

        ///FIXME ??? usable when UserLevels have additional, editable fields
        // code handling edited UserLevels

        accountFacade.edit(foundAccount);

        return foundAccount;
    }

    /**
     * Activate account with a token from activation URL, sent to user e-mail address, specified during registration.
     *
     * @param token Last part of the activation URL sent in a message to users e-mail address.
     * @return Boolean value indicating whether activation of the account was successful or not.
     */
    @Override
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
     * @return Returns true if the e-mail confirmation was successful. Returns false if the token is expired or invalid.
     * @throws AccountNotFoundException Threw if the account connected to the token does not exist.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AccountEmailChangeException.class)
    public boolean confirmEmail(String token) throws AccountNotFoundException, AccountEmailNullException, AccountEmailChangeException {
        Token tokenFromDB = tokenFacade.findByTokenValue(token).orElse(null);
        Optional<Account> accountFromDB = accountFacade.find(jwtProvider.extractAccountId(token));
        Account account = accountFromDB.orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));
        if (tokenFromDB == null || !jwtProvider.isTokenValid(token, account)) {
            return false;
        } else {
            try {
                String email = jwtProvider.extractEmail(token);
                if (email == null) throw new AccountEmailNullException(I18n.ACCOUNT_EMAIL_FROM_TOKEN_NULL_EXCEPTION);
                account.setEmail(email);
                accountFacade.edit(account);
                tokenFacade.remove(tokenFromDB);
                return true;
            } catch (ConstraintViolationException e) {
                throw new AccountEmailChangeException(I18n.ACCOUNT_EMAIL_COLLISION_EXCEPTION);
            }
        }
    }

    /**
     * Retrieve Account that match the parameters, in a given order.
     *
     * @param login      Account's login. A phrase is sought in the logins.
     * @param firstName  Account's owner first name. A phrase is sought in the names.
     * @param lastName   Account's owner last name. A phrase is sought in the last names.
     * @param order      Ordering in which user accounts should be returned.
     * @param pageNumber Number of the page with searched users accounts.
     * @param pageSize   Number of the users accounts per page.
     * @return List of user accounts that match the given parameters.
     */
    @Override
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
     * @param pageNumber The page number of the results to return.
     * @param pageSize   The number of results to return per page.
     * @return A list of all accounts in the system, ordered by account login, with pagination applied.
     */
    @Override
    public List<Account> getAllAccounts(int pageNumber, int pageSize) {
        return accountFacade.findAllAccountsWithPagination(pageNumber, pageSize);
    }

    /**
     * Retrieves an Account by the login.
     *
     * @param login Login of the searched user account.
     * @return If Account with the given login was found returns Account, otherwise returns null.
     */
    @Override
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
    @Override
    public Optional<Account> getAccountById(UUID id) {
        return accountFacade.findAndRefresh(id);
    }

    /**
     * Changes the e-mail of the specified Account.
     *
     * @param accountId ID of the account which the e-mail will be changed.
     * @param newEmail  New e-mail address.
     * @throws AccountEmailChangeException Threw if any problem related to the e-mail occurs.
     *                                     Contains a key to an internationalized message.
     *                                     Additionally, if the problem was caused by an incorrect new mail,
     *                                     the cause is set to <code>AccountValidationException</code> which contains more details about the incorrect fields.
     * @throws AccountNotFoundException    Threw if account with specified Id can't be found.
     */
    @Override
    public void changeEmail(UUID accountId, String newEmail) throws AccountEmailChangeException, AccountNotFoundException {
        Account account = accountFacade.find(accountId).orElseThrow(AccountNotFoundException::new);
        if (Objects.equals(account.getEmail(), newEmail))
            throw new AccountEmailChangeException(I18n.ACCOUNT_SAME_EMAIL_EXCEPTION);
        if (accountFacade.findByEmail(newEmail).isPresent())
            throw new AccountEmailChangeException(I18n.ACCOUNT_EMAIL_COLLISION_EXCEPTION);

        var token = tokenService.createEmailConfirmationToken(account, newEmail);
        //TODO make it so the URL is based on some property
        String confirmationURL = "http://localhost:8080/api/v1/account/change-email/" + token;
        mailProvider.sendEmailConfirmEmail(account.getName(), account.getLastname(), newEmail, confirmationURL, account.getAccountLanguage());
    }

    /**
     * Creates a new JWT related to changing of an account's e-mail,
     * replaces old JWT in the Token in database and sends new confirmation e-mail.
     *
     * @throws AccountNotFoundException Thrown when account from security context can't be found in the database.
     * @throws TokenNotFoundException   Thrown when there is no e-mail confirmation token related to the given account in the database.
     */
    @Override
    public void resendEmailConfirmation() throws AccountNotFoundException, TokenNotFoundException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountFacade.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        Token dbToken = tokenFacade.findByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL, account.getId())
                .orElseThrow(() -> new TokenNotFoundException(I18n.TOKEN_NOT_FOUND_EXCEPTION));

        String newEmail = jwtProvider.extractEmail(dbToken.getTokenValue());
        //TODO change ttl to be a parameter set somewhere in properties
        String newTokenValue = jwtProvider.generateEmailToken(account, newEmail, 24);

        String confirmationURL = "http://localhost:8080/api/v1/account/change-email/" + newTokenValue;
        mailProvider.sendEmailConfirmEmail(account.getName(), account.getLastname(), newEmail, confirmationURL, account.getAccountLanguage());

        dbToken.setTokenValue(newTokenValue);
        tokenFacade.edit(dbToken);
    }
}
