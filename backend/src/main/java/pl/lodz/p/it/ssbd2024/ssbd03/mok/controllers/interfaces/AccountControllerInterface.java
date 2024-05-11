package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.UUID;

/**
 * Interface used for managing Accounts
 */
public interface AccountControllerInterface {

    /**
     * This method is used to block specified account.
     *
     * @param id Identifier of account that will be blocked.
     * @return HTTP response with a code depending on the result.
     */
    ResponseEntity<?> blockAccount(String id);

    /**
     * This method allows to unblock a user account by its UUID.
     *
     * @param id Identifier of account that will be unblocked.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> unblockAccount(String id);

    /**
     * This endpoint is used to "forget" password for an unauthenticated user. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param accountEmailDTO Data transfer object containing unauthenticated user e-mail address, used for registration
     *                        to the application or changed later to other e-mail address.
     *
     * @return 204 NO CONTENT if entire process of forgetting password is successful. Otherwise, 404 NOT FOUND could be returned
     * (if there is no account with given e-mail address) or 412 PRECONDITION FAILED (when account is either blocked or
     * not activated yet).
     *
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method.
     */
    ResponseEntity<?> forgetAccountPassword(AccountEmailDTO accountEmailDTO) throws ApplicationBaseException;

    /**
     * This endpoint is used to change password for an unauthenticated user. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param token RESET PASSWORD token required to change password for user account, that was generated when
     *              forgetAccountPassword() method was called.
     *
     * @return 200 OK is returned when changing password goes flawlessly. Otherwise, 400 BAD REQUEST is returned (since
     * RESET PASSWORD token is no longer valid or not in the database).
     */
    ResponseEntity<?> changeAccountPassword(String token, AccountPasswordDTO accountPasswordDTO) throws ApplicationBaseException;

    /**
     * This method retrieves user accounts from the system.
     *
     * @param pageNumber Number of the page, which user accounts will be retrieved from.
     * @param pageSize Number of user accounts per page.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> getAllUsers(int pageNumber, int pageSize);

    /**
     * This method is used to retrieve user accounts that match specified criteria.
     *
     * @param login Login of the searched user account. Its default value is empty string (in that case this parameter will not have any impact of final result of the search).
     * @param firstName First name of the searched users.
     * @param lastName Last name of the searched users.
     * @param active Activity status of the user account (whether it has been activated or not).
     * @param order Ordering of the searched users. Could be either true (for ascending order) or false (for descending order).
     * @param pageNumber Number of the page containing searched users.
     * @param pageSize Number of the users per page.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> getAccountsByMatchingLoginFirstNameAndLastName(String login, String firstName, String lastName, boolean active, boolean order, int pageNumber, int pageSize);

    /**
     * This method is used to activate user account, after it was successfully.
     * registered either by user itself or by user with administrative privileges.
     *
     * @param token Last part of the activation URL, sent to the e-mail address user specified during registration process. It is a JWT token
     *              generated with payload taken from the user account (id and login) and is valid for a certain amount of time.
     * @return It returns an HTTP response with a code depending on the result.
     * @throws ApplicationBaseException General superclass for all application exceptions, thrown by the aspects intercepting
     *      * methods in both facade and service component for Account.
     */
    ResponseEntity<?> activateAccount(String token) throws ApplicationBaseException;

    /**
     * This method is used to confirm the change of an e-mail.
     *
     * @param token Last part of the activation URL, sent to the new e-mail address. It is a JWT token
     *              generated with payload taken from the user account (id and login) and is valid for a certain amount of time.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> confirmEmail(String token);

    /**
     * This method is used to modify personal data of currently logged-in user.
     *
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> getSelf() throws ApplicationBaseException;

    /**
     * This method is used to modify personal data of currently logged-in user.
     *
     * @param ifMatch Value of If-Match header
     * @param accountModifyDTO Account properties with potentially changed values.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> modifySelfAccount(String ifMatch, AccountModifyDTO accountModifyDTO) throws ApplicationBaseException;

    /**
     * This method is used to find user account by Id.
     *
     * @param id Identifier of account to find.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> getUserById(String id) throws ApplicationBaseException;

    /**
     * This method is used to change users e-mail address, which later could be used to send
     * messages about user actions in the application (e.g. messages containing confirmation links).
     *
     * @param id                    Identifier of the user account, whose e-mail will be changed by this method.
     * @param accountEmailDTO Data transfer object containing new e-mail address.
     * @return                      It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> changeEmail(UUID id, AccountEmailDTO accountEmailDTO);

    /**
     * This method is used to resend confirmation e-mail message.
     * It generates a new token used in a confirmation.
     *
     * @return This method returns 200 OK if the mail with new e-mail confirmation message was successfully sent.
     */
    ResponseEntity<?> resendEmailConfirmation();

    /**
     * This method is used to remove client user level from account.
     *
     * @param id    Identifier of the user account, whose user level will be changed by this method.
     * @return      If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     *             could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     *             If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     *             explaining why the error occurred).
     */
    ResponseEntity<?> removeClientUserLevel(String id);

    /**
     * This method is used to remove staff user level from account.
     *
     * @param id    Identifier of the user account, whose user level will be changed by this method.
     * @return      If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     *             could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     *             If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     *             explaining why the error occurred).
     */
    ResponseEntity<?> removeStaffUserLevel(String id);

    /**
     * This method is used to remove admin user level from account.
     *
     * @param id    Identifier of the user account, whose user level will be changed by this method.
     * @return      If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     *             could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     *             If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     *             explaining why the error occurred).
     */
    ResponseEntity<?> removeAdminUserLevel(String id);
}
