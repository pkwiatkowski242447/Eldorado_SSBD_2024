package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountChangePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.UUID;

/**
 * Interface used for managing Accounts
 */
public interface AccountControllerInterface {

    /**
     * This endpoint allows user with administrative user level to block a user account by its UUID. After that
     * the user account is blocked and could not authenticate to the application.
     *
     * @param id UUID of an Account to block.
     * @return If account blocking is successful, then 204 NO CONTENT is returned as a response.
     * In case of IllegalArgumentException or AccountNotFoundException being thrown, during parsing passed id from String to UUID class,
     * 400 BAD REQUEST is returned, with appropriate message. If AccountAlreadyBlockedException or IllegalOperationException is thrown,
     * the response is 409 CONFLICT. 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> blockAccount(String id) throws ApplicationBaseException;

    /**
     * This endpoint allows user with administrative user level to unblock a user account by its UUID. After that
     * the user account is unblocked, and it could authenticate in the application.
     *
     * @param id UUID of an Account to unblock.
     * @return If account unblocking is successful, then 204 NO CONTENT is returned as a response.
     * In case of IllegalArgumentException or AccountNotFoundException being thrown, during parsing passed id from String to UUID class,
     * 400 BAD REQUEST is returned, with appropriate message. If AccountAlreadyUnblockedException is thrown, the response is 409 CONFLICT.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> unblockAccount(String id) throws ApplicationBaseException;

    /**
     * This endpoint is used to "forget" password for an unauthenticated user. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param accountEmailDTO Data transfer object containing unauthenticated user e-mail address, used for registration
     * to the application or changed later to other e-mail address.
     * @return 204 NO CONTENT if entire process of forgetting password is successful. Otherwise, 404 NOT FOUND could be returned
     * (if there is no account with given e-mail address) or 400 BAD REQUEST (when account is either blocked or
     * not activated yet).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> forgetAccountPassword(AccountEmailDTO accountEmailDTO) throws ApplicationBaseException;

    /**
     * This endpoint is used to reset user account password by the administrator. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param id Identifier of the account of which the password will be reset.
     * @return 204 NO CONTENT if entire process of resetting password is successful. Otherwise, 404 NOT FOUND could be returned
     * (if there is no account with given e-mail address) or 400 BAD REQUEST (when account is either blocked or
     * not activated yet).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> resetAccountPassword(String id) throws ApplicationBaseException;

    /**
     * This endpoint is used to change password for an unauthenticated user. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param token RESET PASSWORD token required to change password for user account, that was generated when
     * forgetAccountPassword() method was called.
     * @return 200 OK is returned when changing password goes flawlessly. Otherwise, 400 BAD REQUEST is returned (since
     * RESET PASSWORD token is no longer valid or not in the database).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> changeAccountPassword(String token, AccountPasswordDTO accountPasswordDTO) throws ApplicationBaseException;

    /**
     * This method retrieves user accounts from the system. In order to avoid sending huge amounts of user data
     * it used pagination, so that user accounts from a particular page, of a particular size can be retrieved.
     *
     * @param pageNumber Number of the page, which user accounts will be retrieved from.
     * @param pageSize   Number of user accounts per page.
     * @return This method returns 200 OK as a response, where in response body a list of user accounts is located, is a JSON format.
     * If the list is empty (there are not user accounts in the system), this method would return 204 NO CONTENT as the response.
     * @note. This method retrieves all users accounts, not taking into consideration their role. The results are ordered by
     * login alphabetically.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> getAllUsers(int pageNumber, int pageSize) throws ApplicationBaseException;

    /**
     * This method is used to retrieve user accounts that match specified criteria.
     *
     * @param login      Login of the searched user account. Its default value is empty string (in that case this parameter will not have any impact of final result of the search).
     * @param firstName  First name of the searched users.
     * @param lastName   Last name of the searched users.
     * @param order      Ordering of the searched users. Could be either true (for ascending order) or false (for descending order).
     * @param pageNumber Number of the page containing searched users.
     * @param pageSize   Number of the users per page.
     * @return This method returns 200 OK response, with list of users in the response body, converted to JSON.
     * If the list is empty, then 204 NO CONTENT is returned.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> getAccountsByMatchingLoginFirstNameAndLastName(
            String login, String firstName, String lastName, boolean active, boolean order, int pageNumber, int pageSize)
            throws ApplicationBaseException;

    /**
     * This method is used to activate user account, after it was successfully.
     * registered either by user itself or by user with administrative privileges.
     *
     * @param token Last part of the activation URL, sent to the e-mail address user specified during registration process. It is a JWT token
     *              generated with payload taken from the user account (id and login) and is valid for a certain amount of time.
     * @return It returns an HTTP response with a code depending on the result.
     * @throws ApplicationBaseException General superclass for all application exceptions, thrown by the aspects intercepting
     * methods in both facade and service component for Account.
     */
    ResponseEntity<?> activateAccount(String token) throws ApplicationBaseException;

    /**
     * This method is used to activate user account, after it was successfully
     * registered either by user itself or by user with administrative privileges.
     *
     * @param token Last part of the activation URL, sent to the e-mail address user specified during registration process. It is a JWT token
     * generated with payload taken from the user account (id and login) and is valid for a certain amount of time.
     * @return This function returns 204 NO CONTENT if method finishes successfully (all performed action finish without any errors).
     * It could also return 204 NO CONTENT if the token is not valid.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> confirmEmail(String token) throws ApplicationBaseException;

    /**
     * This method is used to find user account of currently logged-in user.
     *
     * @return If user account is found for currently logged user then 200 OK with user account in the response
     * body is returned, otherwise 400 BAD REQUEST is returned, since user account could not be found.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> getSelf() throws ApplicationBaseException;

    /**
     * This method is used to modify personal data of currently logged-in user.
     *
     * @param ifMatch          Value of If-Match header
     * @param accountModifyDTO Account properties with potentially changed values.
     * @return In the correct flow returns account object with applied modifications with 200 OK status.
     * If request has empty IF_MATCH header or account currently logged user was not found or data are invalid
     * 400 BAD REQUEST is returned. If accountModifyDTO signature is different from IF_MATCH header value
     * then 409 CONFLICT is returned.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> modifyAccountSelf(String ifMatch, AccountModifyDTO accountModifyDTO) throws ApplicationBaseException;

    /**
     * This method is used to modify personal data user by other user with administrative privileges.
     *
     * @param ifMatch          Value of If-Match header
     * @param accountModifyDTO Account properties with potentially changed values.
     * @return In the correct flow returns account object with applied modifications with 200 OK status.
     * If request has empty IF_MATCH header or account currently logged user was not found or data are invalid
     * 400 BAD REQUEST is returned. If accountModifyDTO signature is different from IF_MATCH header value
     * then 409 CONFLICT is returned.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> modifyUserAccount(String ifMatch, AccountModifyDTO accountModifyDTO) throws ApplicationBaseException;

    /**
     * This method is used to find user account by id.
     *
     * @param id Identifier of account to find.
     * @return It returns HTTP response 200 OK with user information if account exists. If account with id doesn't exist
     * returns 404. When uuid is invalid returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> getUserById(String id) throws ApplicationBaseException;

    /**
     * This method is used to change users e-mail address, which later could be used to send
     * messages about user actions in the application (e.g. messages containing confirmation links).
     *
     * @param id              Identifier of the user account, whose e-mail will be changed by this method.
     * @param accountEmailDTO Data transfer object containing new e-mail address.
     * @return If changing e-mail address is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore e-mail address could not be changed) then 400 BAD REQUEST is returned. If account
     * is found but new e-mail does not follow constraints, then 500 INTERNAL SERVER ERROR is returned (with a message
     * explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> changeEmail(UUID id, AccountEmailDTO accountEmailDTO) throws ApplicationBaseException;

    /**
     * This method allows user to change their e-mail address, which later could be used to send
     * messages about user actions in the application (e.g. messages containing confirmation links).
     *
     * @param accountEmailDTO Data transfer object containing new e-mail address.
     * @return If changing e-mail address is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore e-mail address could not be changed) then 400 BAD REQUEST is returned. If account
     * is found but new e-mail does not follow constraints, then 500 INTERNAL SERVER ERROR is returned (with a message
     * explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> changeEmailSelf(AccountEmailDTO accountEmailDTO) throws ApplicationBaseException;

    /**
     * This method is used to resend confirmation e-mail message.
     * It generates a new token used in a confirmation.
     *
     * @return This method returns 200 OK if the mail with new e-mail confirmation message was successfully sent.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> resendEmailConfirmation() throws ApplicationBaseException;

    /**
     * This method is used to remove client user level from account.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 400 BAD REQUEST is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> removeClientUserLevel(String id) throws ApplicationBaseException;

    /**
     * This method is used to remove staff user level from account.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 400 BAD REQUEST is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> removeStaffUserLevel(String id) throws ApplicationBaseException;

    /**
     * This method is used to remove admin user level from account.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 400 BAD REQUEST is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> removeAdminUserLevel(String id) throws ApplicationBaseException;

    /**
     * This method is used to client user level to the user account with given identifier which
     * is passed as a String to this method.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If adding user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned
     * (with a message explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> addClientUserLevel(String id) throws ApplicationBaseException;

    /**
     * This method is used to staff user level to the user account with given identifier which
     * is passed as a String to this method.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If adding user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned
     * (with a message explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> addStaffUserLevel(String id) throws ApplicationBaseException;

    /**
     * This method is used to admin user level to the user account with given identifier which
     * is passed as a String to this method.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If adding user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned
     * (with a message explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> addAdminUserLevel(String id) throws ApplicationBaseException;

    /**
     * This method is used to change own password.
     *
     * @param accountChangePasswordDTO Data transfer object containing old Password and new password.
     * @return If password successfully changed returns 200 OK Http response. If old password is incorrect or new password
     * is the same as current password returns 400 BAD REQUEST HTTP response.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     * exception handling aspects from facade and service layers below.
     */
    ResponseEntity<?> changePasswordSelf(AccountChangePasswordDTO accountChangePasswordDTO) throws ApplicationBaseException;
}




