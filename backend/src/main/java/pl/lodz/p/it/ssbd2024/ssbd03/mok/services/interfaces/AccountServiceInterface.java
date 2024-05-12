package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountUserLevelException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyUnblockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.old.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface used for managing Accounts
 */
public interface AccountServiceInterface {

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
     * @throws ApplicationBaseException Superclass for all exceptions that could be thrown by the aspects, intercepting facade method invocation.
     */
    void registerClient(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws ApplicationBaseException;

    /**
     * This method is used to create new account, which will have default user level of Staff.
     *
     * @param login       User login, used in order to authenticate to the application.
     * @param password    User password, used in combination with login to authenticate to the application.
     * @param firstName   First name of the user.
     * @param lastName    Last name of the user.
     * @param email       Email address, which will be used to send messages (e.g. confirmation messages) for actions in the application.
     * @param phoneNumber Phone number of the user.
     * @param language    Predefined language constant used for internationalizing all messages for user (initially browser constant value but could be set).
     * @throws ApplicationBaseException Superclass for all exceptions that could be thrown by the aspects, intercepting facade method invocation.
     */
    void registerStaff(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws ApplicationBaseException;

    /**
     * This method is used to create new account, which will have default user level of Admin.
     *
     * @param login       User login, used in order to authenticate to the application.
     * @param password    User password, used in combination with login to authenticate to the application.
     * @param firstName   First name of the user.
     * @param lastName    Last name of the user.
     * @param email       Email address, which will be used to send messages (e.g. confirmation messages) for actions in the application.
     * @param phoneNumber Phone number of the user.
     * @param language    Predefined language constant used for internationalizing all messages for user (initially browser constant value but could be set).
     * @throws ApplicationBaseException Superclass for all exceptions that could be thrown by the aspects, intercepting facade method invocation.
     */
    void registerAdmin(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws ApplicationBaseException;

    /**
     * This method is used to reset current user account password. This method basically generates a token of type
     * CHANGE_PASSWORD and writes it to the database, and then sends a password change URL to the e-mail address
     * specified by the user in the form and send to the application with the usage of DTO object.
     *
     * @param userEmail Email address that will be used to search for the existing account, and then used for sending
     *                  e-mail message with password change URL.
     */
    void forgetAccountPassword(String userEmail) throws ApplicationBaseException;

    /**
     * This method is used to change password of the user. This method does read RESET PASSWORD token with
     * specified token value, and then
     *
     * @param token         Value of the token, that will be used to find RESET PASSWORD token in the database.
     * @param newPassword   New password, transferred to the web application by data transfer object.
     *
     * @throws ApplicationBaseException General superclass for all exceptions thrown by the aspects intercepting that
     * method.
     */
    void changeAccountPassword(String token, String newPassword) throws ApplicationBaseException;

    /**
     * Method for blocking an account by its UUID.
     *
     * @param id Account identifier
     * @throws AccountNotFoundException       Threw when there is no account with given login.
     * @throws AccountAlreadyBlockedException Threw when the account is already blocked.
     * @throws IllegalOperationException      Threw when user try to block their own account.
     */
    void blockAccount(UUID id) throws AccountNotFoundException, AccountAlreadyBlockedException, IllegalOperationException;

    /**
     * Method for unblocking an account by its UUID.
     *
     * @param id Account identifier
     * @throws AccountNotFoundException         Threw when there is no account with given login.
     * @throws AccountAlreadyUnblockedException Threw when the account is already unblocked.
     */
    void unblockAccount(UUID id) throws AccountNotFoundException, AccountAlreadyUnblockedException;

    /**
     * This method is used to modify user personal data.
     *
     * @param modifiedAccount Account with potentially modified properties: name, lastname, phoneNumber.
     * @param currentUserLogin Login associated with the modified account.
     * @return Account object with applied modifications
     * @throws AccountNotFoundException Threw if the account with passed login property does not exist.
     * @throws ApplicationOptimisticLockException Threw if while editing the account, a parallel editing action occurred.
     */
    Account modifyAccount(Account modifiedAccount, String currentUserLogin) throws AccountNotFoundException, ApplicationOptimisticLockException;

    /**
     * Changes the e-mail of the specified Account.
     *
     * @param accountId ID of the account which the e-mail will be changed.
     * @param newEmail  New e-mail address.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects in facade layer.
     */
    void changeEmail(UUID accountId, String newEmail) throws ApplicationBaseException;

    /**
     * Activate account with a token from activation URL, sent to user e-mail address, specified during registration.
     *
     * @param token Last part of the activation URL sent in a message to users e-mail address.
     * @return Boolean value indicating whether activation of the account was successful or not.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by aspects intercepting this method.
     */
    boolean activateAccount(String token) throws ApplicationBaseException;

    /**
     * Confirm e-mail change with a token from confirmation URL, sent to the new e-mail address.
     *
     * @param token Last part of the confirmation URL sent in a message to user's e-mail address.
     * @return Returns true if the e-mail confirmation was successful. Returns false if the token is expired or invalid.
     * @throws AccountNotFoundException Threw if the account connected to the token does not exist.
     */
    boolean confirmEmail(String token) throws ApplicationBaseException;

    /**
     * Retrieve Accounts that match the parameters, in a given order.
     *
     * @param login      Account's login. A phrase is sought in the logins.
     * @param firstName  Account owner's first name. A phrase is sought in the names.
     * @param lastName   Account's owner last name. A phrase is sought in the last names.
     * @param active     Activity status of the user account (whether it has been activated or not).
     * @param order      Ordering in which user accounts should be returned.
     * @param pageNumber Number of the page with searched users accounts.
     * @param pageSize   Number of the users accounts per page.
     * @return List of user accounts that match the given parameters.
     */
    List<Account> getAccountsByMatchingLoginFirstNameAndLastName(String login, String firstName, String lastName, boolean active, boolean order, int pageNumber, int pageSize);

    /**
     * Retrieve all accounts in the system.
     *
     * @param pageNumber The page number of the results to return.
     * @param pageSize   The number of results to return per page.
     * @return A list of all accounts in the system, ordered by account login, with pagination applied.
     */
    List<Account> getAllAccounts(int pageNumber, int pageSize);

    /**
     * Retrieves an Account by the login.
     *
     * @param login Login of the searched user account.
     * @return If an Account with the given login was found return Account, otherwise returns null.
     */
    Account getAccountByLogin(String login);

    /**
     * Retrieves from the database account by id.
     *
     * @param id Account's id.
     * @return Returns Optional containing the requested account if found, otherwise returns empty Optional.
     */
    Optional<Account> getAccountById(UUID id);

    /**
     * Creates a new JWT related to changing of an account's e-mail,
     * replaces old JWT in the Token in database and sends new confirmation e-mail.
     *
     * @throws AccountNotFoundException Thrown when an account from security context can't be found in the database.
     * @throws TokenNotFoundException   Thrown when there is no e-mail confirmation token related to the given account in the database.
     */
    void resendEmailConfirmation() throws ApplicationBaseException;

    /**
     * Removes the client user level from the account.
     *
     * @param id Account's id.
     * @throws AccountNotFoundException  Threw when there is no account with given login.
     * @throws AccountUserLevelException Threw when the account has no client user level or this is the only user level of the account.
     */
    void removeClientUserLevel(String id) throws AccountNotFoundException, AccountUserLevelException;

    /**
     * Removes the staff user level from the account.
     *
     * @param id Account's id.
     * @throws AccountNotFoundException  Threw when there is no account with given login.
     * @throws AccountUserLevelException Threw when the account has no staff user level or this is the only user level of the account.
     */
    void removeStaffUserLevel(String id) throws AccountNotFoundException, AccountUserLevelException;

    /**
     * Removes the admin user level from the account.
     *
     * @param id Account's id.
     * @throws AccountNotFoundException  Threw when there is no account with given login.
     * @throws AccountUserLevelException Threw when the account has no admin user level or this is the only user level of the account.
     */
    void removeAdminUserLevel(String id) throws AccountNotFoundException, AccountUserLevelException;

    /**
     * Adds the Client user level to the account.
     *
     * @param id Account's id.
     * @throws ApplicationBaseException
     * AccountNotFoundException - when account is not found
     * AccountUserLevelException - when account already has this user level
     */
    void addClientUserLevel(String id) throws ApplicationBaseException;

    /**
     *
     * Adds the Staff user level to the account.
     *
     * @param id Account's id.
     * @throws ApplicationBaseException
     * AccountNotFoundException - when account is not found
     * AccountUserLevelException - when account already has this user level
     */
    void addStaffUserLevel(String id) throws ApplicationBaseException;

    /**
     * Adds the Admin user level to the account.
     *
     * @param id Account's id.
     * @throws ApplicationBaseException
     * AccountNotFoundException - when account is not found
     * AccountUserLevelException - when account already has this user level
     */
    void addAdminUserLevel(String id) throws ApplicationBaseException;

    /** Change own password.
     *
     * @param oldPassword The OldPassword is the old password that the user must provide for authentication.
     * @param newPassword The new password is the password that the user wants to set.
     * @param login The login retrieved from the security context.
     * @throws ApplicationBaseException Threw when credentials are incorrect or account not found.
     */
    void changePasswordSelf(String oldPassword, String newPassword, String login) throws ApplicationBaseException;
}
