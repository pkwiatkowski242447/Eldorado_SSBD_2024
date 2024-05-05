package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountChangeEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountModifyDTO;

import java.util.UUID;

/**
 * Interface used for managing Accounts
 */
public interface AccountControllerInterface {

    /**
     * This method is used to block specified account.
     *
     * @param id Id of account that will be blocked.
     * @return HTTP response with a code depending on the result.
     */
    ResponseEntity<?> blockAccount(String id);

    /**
     *
     * @param id Id of account that will be unblocked.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> unblockAccount(String id);

    /**
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
     * @param order Ordering of the searched users. Could be either true (for ascending order) or false (for descending order).
     * @param pageNumber Number of the page containing searched users.
     * @param pageSize Number of the users per page.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> getAccountsByMatchingLoginFirstNameAndLastName(String login, String firstName, String lastName, boolean order, int pageNumber, int pageSize);

    /**
     * This method is used to activate user account, after it was successfully.
     * registered either by user itself or by user with administrative privileges.
     *
     * @param token Last part of the activation URL, sent to the e-mail address user specified during registration process. It is a JWT token
     *              generated with payload taken from the user account (id and login) and is valid for a certain amount of time.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> activateAccount(String token);

    /**
     * This method is used to confirm the change of an e-mail
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
    ResponseEntity<?> getSelf();

    /**
     * This method is used to modify personal data of currently logged-in user.
     *
     * @param ifMatch Value of If-Match header
     * @param accountModifyDTO Account properties with potentially changed values.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> modifySelfAccount(String ifMatch, AccountModifyDTO accountModifyDTO);

    /**
     * This method is used to find user account by Id.
     *
     * @param id Id of account to find.
     * @return It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> getUserById(String id);

    /**
     * This method is used to change users e-mail address, which later could be used to send
     * messages about user actions in the application (e.g. messages containing confirmation links).
     *
     * @param id                    Identifier of the user account, whose e-mail will be changed by this method.
     * @param accountChangeEmailDTO Data transfer object containing new e-mail address.
     * @return                      It returns an HTTP response with a code depending on the result.
     */
    ResponseEntity<?> changeEmail(UUID id, AccountChangeEmailDTO accountChangeEmailDTO);

    /**
     * This method is used to resend confirmation e-mail message.
     * It generates a new token used in a confirmation.
     *
     * @return This method returns 200 OK if the mail with new e-mail confirmation message was successfully sent.
     */
    ResponseEntity<?> resendEmailConfirmation();
}
