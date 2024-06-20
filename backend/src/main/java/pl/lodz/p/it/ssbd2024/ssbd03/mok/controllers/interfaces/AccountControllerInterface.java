package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountChangePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountPasswordDTO;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.UUID;

/**
 * Interface of the controller, used for managing Accounts
 */
public interface AccountControllerInterface {

    // Block & unblock account methods

    /**
     * This endpoint allows user with administrative user level to block a user account by its UUID. After that
     * the user account is blocked and could not authenticate to the application.
     *
     * @param id UUID of an Account to be blocked.
     * @return If account blocking is successful, then 204 NO CONTENT is returned as a response.
     * In case of IllegalArgumentException or AccountNotFoundException being thrown, during parsing passed id from String to UUID class,
     * 400 BAD REQUEST is returned, with appropriate message. If AccountAlreadyBlockedException or IllegalOperationException is thrown,
     * the response is 409 CONFLICT. 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/block", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Block user account", description = "The endpoint is used to block user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The account has been blocked correctly."),
            @ApiResponse(responseCode = "400", description = "The account has not been blocked due to the correctness of the request or because the account is not available in the database"),
            @ApiResponse(responseCode = "409", description = "The account has not been blocked due to being blocked already or trying to block own account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> blockAccount(@PathVariable("id") String id)
            throws ApplicationBaseException;

    /**
     * This endpoint allows user with administrative user level to unblock a user account by its UUID. After that
     * the user account is unblocked, and it could authenticate in the application.
     *
     * @param id UUID of an Account to be unblocked.
     * @return If account unblocking is successful, then 204 NO CONTENT is returned as a response.
     * In case of IllegalArgumentException or AccountNotFoundException being thrown, during parsing passed id from String to UUID class,
     * 400 BAD REQUEST is returned, with appropriate message. If AccountAlreadyUnblockedException is thrown, the response is 409 CONFLICT.
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/unblock", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Unblock user account", description = "The endpoint is used to unblock user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The account has been unblocked correctly."),
            @ApiResponse(responseCode = "400", description = "The account has not been unblocked due to the correctness of the request or because the account is not available in the database"),
            @ApiResponse(responseCode = "409", description = "The account has not been unblocked due to being blocked already."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> unblockAccount(@PathVariable("id") String id)
            throws ApplicationBaseException;

    // Password change methods

    /**
     * This endpoint is used to "forget" password for an unauthenticated user. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param accountEmailDTO Data transfer object containing unauthenticated user e-mail address, used for registration
     *                        in the application or changed later to other e-mail address.
     * @return 204 NO CONTENT if entire process of forgetting password is successful. Otherwise, 400 BAD REQUEST could
     * be returned (when account is either blocked, not activated yet or account with given e-mail could not be found).
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/forgot-password")
    @Operation(summary = "Forget currently set password", description = "The endpoint is used to forget current password, that is to send e-mail message with password reset URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The password change URL has been sent to the user's e-mail address."),
            @ApiResponse(responseCode = "400", description = "The account is blocked or not activated or account with given e-mail has not been found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> forgetAccountPassword(@Valid @RequestBody AccountEmailDTO accountEmailDTO)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to reset user account password by the administrator. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param id Identifier of the account of which the password will be reset.
     * @return 204 NO CONTENT if entire process of resetting password is successful. Otherwise, 400 BAD REQUEST could
     * be returned (when account is either blocked, not activated yet or there is no account with given id).
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/reset-password/{id}")
    @Operation(summary = "Reset account password by admin", description = "The endpoint is used by admin to send password change URL to e-mail address attached to the account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The password change URL has been sent to the user's e-mail address."),
            @ApiResponse(responseCode = "400", description = "The account is blocked or not activated or account with given identifier has not been found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> resetAccountPassword(@PathVariable("id") String id)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to change password for an unauthenticated user. It removes generated RESET PASSWORD token with
     * given token value, and set new password for user account.
     *
     * @param token RESET PASSWORD token required to change password for user account, that was generated when
     *              forgetAccountPassword() method was called.
     * @return 200 OK is returned when changing password goes flawlessly. Otherwise, 400 BAD REQUEST is returned (since
     * RESET PASSWORD token is no longer valid or not in the database). 500 INTERNAL SERVER ERROR is returned when
     * other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/change-password/{token}")
    @Operation(summary = "Change account password", description = "The endpoint is used by unauthenticated user to change their account password, by clicking the link sent to their e-mail address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The password was changed successfully."),
            @ApiResponse(responseCode = "400", description = "The account is blocked or not activated or account, which the password is changed for has not been found, or there is no password change token for given account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> changeAccountPassword(@PathVariable("token") String token,
                                            @Valid @RequestBody AccountPasswordDTO accountPasswordDTO)
            throws ApplicationBaseException;

    /**
     * This method is used to change own password.
     *
     * @param accountChangePasswordDTO Data transfer object containing old Password and new password.
     * @return If password successfully changed returns 200 OK Http response. If old password is incorrect or new password
     * is the same as current password returns 400 BAD REQUEST HTTP response. 500 INTERNAL SERVER ERROR is returned
     * when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PatchMapping(value = "/change-password/self", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Change own password", description = "The endpoint is used to change password, used to authenticate to the currently logged in account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 ", description = "Account's password was changed successfully."),
            @ApiResponse(responseCode = "400", description = "Given past password is not the same as the old one, new password is the same as the old one or account, which the password is change for could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> changePasswordSelf(@Valid @RequestBody AccountChangePasswordDTO accountChangePasswordDTO)
            throws ApplicationBaseException;

    // Read methods

    /**
     * This method retrieves user accounts from the system. In order to avoid sending huge amounts of user data
     * it used pagination, so that user accounts from a particular page, of a particular size can be retrieved.
     * This method retrieves all users accounts, not taking into consideration their role. The results are ordered by
     * login alphabetically.
     *
     * @param pageNumber Number of the page, which user accounts will be retrieved from.
     * @param pageSize   Number of user accounts per page.
     * @return This method returns 200 OK as a response, where in response body a list of user accounts is located, is a JSON format.
     * If the list is empty (there are not user accounts in the system), this method would return 204 NO CONTENT as the response.
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all users", description = "The endpoint is used retrieve list of accounts from given page of given size.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of accounts returned from given page of given size is not empty."),
            @ApiResponse(responseCode = "204", description = "List of accounts returned from given page of given size is empty."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getAllUsers(@RequestParam("pageNumber") int pageNumber,
                                  @RequestParam("pageSize") int pageSize)
            throws ApplicationBaseException;

    /**
     * This method is used to retrieve user accounts that match specified criteria.
     *
     * @param phrase     Phrase in account's firstname or lastname.
     * @param orderBy    Either "login" or "level", defaults to "login". If entered incorrectly set as "login".
     * @param order      Ordering of the searched users. Could be either true (for ascending order) or false (for descending order).
     * @param pageNumber Number of the page containing searched users.
     * @param pageSize   Number of the users per page.
     * @return This method returns 200 OK response, with list of users in the response body, converted to JSON.
     * If the list is empty, then 204 NO CONTENT is returned. 500 INTERNAL SERVER ERROR is returned when other unexpected
     * exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/match-phrase-in-account", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all users matching criteria", description = "The endpoint is used retrieve list of accounts that match certain criteria, that is either contain certain phrase in login, firstName, lastName, with certain activity status and ordered by login alphabetically or not.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of accounts returned from given page of given size is not empty."),
            @ApiResponse(responseCode = "204", description = "List of accounts returned from given page of given size is empty."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getAccountsMatchingPhraseInNameOrLastname(@RequestParam(name = "phrase", defaultValue = "") String phrase,
                                                                @RequestParam(name = "orderBy", defaultValue = "login") String orderBy,
                                                                @RequestParam(name = "order", defaultValue = "true") boolean order,
                                                                @RequestParam(name = "pageNumber") int pageNumber,
                                                                @RequestParam(name = "pageSize") int pageSize)
            throws ApplicationBaseException;

    /**
     * This method is used to find user account of currently logged-in user.
     *
     * @return If user account is found for currently logged user then 200 OK with user account in the response
     * body is returned, otherwise 400 BAD REQUEST is returned, since user account could not be found.
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/self", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get your account details", description = "The endpoint is used to get user's own account details, and sign them using JWS, where the signature is placed in ETag header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's accounts details were found successfully."),
            @ApiResponse(responseCode = "400", description = "Account of the currently logged in user could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getSelf()
            throws ApplicationBaseException;

    /**
     * This method is used to find user account by id.
     *
     * @param id Identifier of account to find.
     * @return It returns HTTP response 200 OK with user information if account exists. If account with id doesn't exist
     * returns 404. When uuid is invalid returns 400. 500 INTERNAL SERVER ERROR is returned when other
     * unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find user account by id", description = "The endpoint is used retrieve user account details by its identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's account details were found successfully."),
            @ApiResponse(responseCode = "400", description = "User account's identifier is invalid."),
            @ApiResponse(responseCode = "404", description = "User account could not be found in the database."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getUserById(@PathVariable("id") String id)
            throws ApplicationBaseException;

    // Activate account method

    /**
     * This method is used to activate user account, after it was successfully
     * registered either by user itself or by user with administrative privileges.
     *
     * @param token Last part of the activation URL, sent to the e-mail address user specified during registration process. It is a JWT token
     *              generated with payload taken from the user account (id and login) and is valid for a certain amount of time.
     * @return This function returns 204 NO CONTENT if method finishes successfully (all performed action finish without any errors).
     * It could also return 204 NO CONTENT if the token is not valid. 500 INTERNAL SERVER ERROR is returned when
     * other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping("/activate-account/{token}")
    @Operation(summary = "Activate account", description = "The endpoint is used activate user account by itself after successful registration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account activation was successful."),
            @ApiResponse(responseCode = "400", description = "Activation URL expired, is invalid or account, which is being activated is not found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> activateAccount(@PathVariable("token") String token)
            throws ApplicationBaseException;

    // E-mail change methods

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
     *                                  exception handling aspects from facade and service layers below.
     */
    @PatchMapping(value = "/change-email-self", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Change own e-mail address", description = "The endpoint is used change e-mail address, attached to own user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "E-mail change confirmation message was successfully sent to the current e-mail address attached to the user's account."),
            @ApiResponse(responseCode = "400", description = "Given e-mail address is already set, is already taken by other user or user account could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> changeEmailSelf(@Valid @RequestBody AccountEmailDTO accountEmailDTO)
            throws ApplicationBaseException;

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
     *                                  exception handling aspects from facade and service layers below.
     */
    @PatchMapping(value = "/{id}/change-email", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Change other user's e-mail address", description = "The endpoint is used change e-mail address, attached to certain user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "E-mail change confirmation message was successfully sent to the current e-mail address attached to the user's account."),
            @ApiResponse(responseCode = "400", description = "Given e-mail address is already set, is already taken by other user or user account could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> changeEmail(@PathVariable("id") UUID id,
                                  @Valid @RequestBody AccountEmailDTO accountEmailDTO)
            throws ApplicationBaseException;

    /**
     * This method is used to activate user account, after it was successfully
     * registered either by user itself or by user with administrative privileges.
     *
     * @param token Last part of the activation URL, sent to the e-mail address user specified during registration process. It is a JWT token
     *              generated with payload taken from the user account (id and login) and is valid for a certain amount of time.
     * @return This function returns 204 NO CONTENT if method finishes successfully (all performed action finish without any errors).
     * It could also return 204 NO CONTENT if the token is not valid. 500 INTERNAL SERVER ERROR is returned when
     * other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/confirm-email/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Confirm e-mail", description = "The endpoint is used confirm e-mail attached to user's account after it was changed by the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "E-mail confirmation process was successful."),
            @ApiResponse(responseCode = "400", description = "E-mail confirmation URL expired, is invalid or account, which is e-mail is activated for, is not found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> confirmEmail(@PathVariable("token") String token)
            throws ApplicationBaseException;

    /**
     * This method is used to resend confirmation e-mail message.
     * It generates a new token used in a confirmation.
     *
     * @return This method returns 200 OK if the mail with new e-mail confirmation message was successfully sent.
     * Otherwise, 400 BAD REQUEST is sent when there is no token for e-mail change connected to given account
     * or account with given e-mail address does not exist. 500 INTERNAL SERVER ERROR is returned when other
     * unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/resend-email-confirmation")
    @Operation(summary = "Resend e-mail confirmation message", description = "The endpoint is used resend e-mail confirmation message, that would be used to change e-mail address attached to user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "E-mail change confirmation message is sent successfully."),
            @ApiResponse(responseCode = "400", description = "There is no token for account's e-mail change or the account, which the e-mail is change for, could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> resendEmailConfirmation()
            throws ApplicationBaseException;

    // Modify account methods

    /**
     * This method is used to modify personal data of currently logged-in user.
     *
     * @param ifMatch          Value of If-Match header
     * @param accountModifyDTO Account properties with potentially changed values.
     * @return In the correct flow returns account object with applied modifications with 200 OK status.
     * If request has empty IF_MATCH header or account currently logged user was not found or data are invalid
     * 400 BAD REQUEST is returned. If accountModifyDTO signature is different from IF_MATCH header value
     * then 409 CONFLICT is returned. 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PutMapping(value = "/self", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Modify self account", description = "The endpoint is used to modify self account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The account has been modified correctly.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountOutputDTO.class))),
            @ApiResponse(responseCode = "400", description = "The account has not been modified due to the correctness of the request or because the account is not available in the database", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "409", description = "The account has not been modified due to modification of signed fields.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    ResponseEntity<?> modifyAccountSelf(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                        @Valid @RequestBody AccountModifyDTO accountModifyDTO)
            throws ApplicationBaseException;

    /**
     * This method is used to modify personal data user by other user with administrative privileges.
     *
     * @param ifMatch          Value of If-Match header
     * @param accountModifyDTO Account properties with potentially changed values.
     * @return In the correct flow returns account object with applied modifications with 200 OK status.
     * If request has empty IF_MATCH header or account currently logged user was not found or data are invalid
     * 400 BAD REQUEST is returned. If accountModifyDTO signature is different from IF_MATCH header value
     * then 409 CONFLICT is returned. 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Modify other user account account", description = "The endpoint is used to modify user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The account has been modified correctly.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountOutputDTO.class))),
            @ApiResponse(responseCode = "400", description = "The account has not been modified due to the correctness of the request or because the account is not available in the database", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "409", description = "The account has not been modified due to parallel modification.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    ResponseEntity<?> modifyUserAccount(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                        @Valid @RequestBody AccountModifyDTO accountModifyDTO)
            throws ApplicationBaseException;

    // Add user level methods - Client, Staff, Admin

    /**
     * This method is used to client user level to the user account with given identifier which
     * is passed as a String to this method.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If adding user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned
     * (with a message explaining why the error occurred).500 INTERNAL SERVER ERROR is returned when other unexpected
     * exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/add-level-client", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add client user level", description = "The endpoint is used to add client user level to the account with given identifier, if the account does not have such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Client user level was added to the account successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does already have client user level, or all user levels are assigned to the account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> addClientUserLevel(@PathVariable("id") String id)
            throws ApplicationBaseException;

    /**
     * This method is used to staff user level to the user account with given identifier which
     * is passed as a String to this method.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If adding user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned
     * (with a message explaining why the error occurred). 500 INTERNAL SERVER ERROR is returned when other
     * unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/add-level-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add staff user level", description = "The endpoint is used to add staff user level to the account with given identifier, if the account does not have such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Staff user level was added to the account successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does already have staff user level, or all user levels are assigned to the account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> addStaffUserLevel(@PathVariable("id") String id)
            throws ApplicationBaseException;

    /**
     * This method is used to admin user level to the user account with given identifier which
     * is passed as a String to this method.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If adding user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned
     * (with a message explaining why the error occurred). 500 INTERNAL SERVER ERROR is returned when other
     * unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/add-level-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add admin user level", description = "The endpoint is used to add admin user level to the account with given identifier, if the account does not have such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Admin user level was added to the account successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does already have admin user level, or all user levels are assigned to the account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> addAdminUserLevel(@PathVariable("id") String id)
            throws ApplicationBaseException;

    // Remove user level methods - Client, Staff, Admin

    /**
     * This method is used to remove client user level from account.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 400 BAD REQUEST is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred). 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/remove-level-client", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove client user level", description = "The endpoint is used to remove client user level from account with given identifier, if the account contains such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Client user level was removed successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does not have client user level, or it is the only user level that account has."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> removeClientUserLevel(@PathVariable("id") String id)
            throws ApplicationBaseException;

    /**
     * This method is used to remove staff user level from account.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 400 BAD REQUEST is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred). 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/remove-level-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove staff user level", description = "The endpoint is used to remove staff user level from account with given identifier, if the account contains such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Staff user level was removed successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does not have staff user level, or it is the only user level that account has."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> removeStaffUserLevel(@PathVariable("id") String id)
            throws ApplicationBaseException;

    /**
     * This method is used to remove admin user level from account.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 400 BAD REQUEST is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred). 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/{id}/remove-level-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove admin user level", description = "The endpoint is used to remove staff user level from account with given identifier, if the account contains such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Admin user level was removed successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does not have admin user level, or it is the only user level that account has, or it is the user level of the currently logged in user."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> removeAdminUserLevel(@PathVariable("id") String id)
            throws ApplicationBaseException;

    // Restore access to user account methods

    /**
     * This endpoint is used to restore access to the user account, which was blocked for not being active in
     * the application for given amount of time. It basically generates restore access token, and sends the restore
     * URL to the e-mail address of the user.
     *
     * @param accountEmailDTO Data transfer object containing unauthenticated user e-mail address, used for restoring
     *                        access to the user account, which was blocked for not being active too long.
     * @return 204 NO CONTENT if entire process of sending restore message is successful. Otherwise, 400 BAD REQUEST could
     * be returned (when account is either blocked, is active or account with given e-mail could not be found).
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/restore-access")
    @Operation(summary = "Generate account access restore token.", description = "The endpoint is used to generate access restore token, that is to send e-mail message with access restore URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The restore access URL has been sent to the user's e-mail address."),
            @ApiResponse(responseCode = "400", description = "The account is blocked, is activated or the account with given e-mail has not been found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> sendAccountRestorationEmailMessage(@Valid @RequestBody AccountEmailDTO accountEmailDTO)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to restore access to the user account, which was blocked for not being active in
     * the application for given amount of time. It basically searches for access restore token, removes it and then
     * changes status of the account from inactive to active, which user can authenticate after.
     *
     * @param tokenValue Value of the access restore token, used for finishing account access restoration process.
     * @return 204 NO CONTENT if entire process of sending restore message is successful. Otherwise, 400 BAD REQUEST could
     * be returned (when account is either blocked, is active or account with given e-mail could not be found).
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/restore-token/{token}")
    @Operation(summary = "Use generated token to restore access to the account.", description = "The endpoint is used to use restore token in the user to restore access to the account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account access restoration process was successful."),
            @ApiResponse(responseCode = "400", description = "The account is blocked, is activated or the account with given e-mail has not been found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> restoreAccountAccess(@PathVariable("token") String tokenValue)
            throws ApplicationBaseException;

    /**
     * This endpoint allows to check whether the account has a password reset request made by an admin.
     *
     * @return Boolean representing whether there is a password reset request made by an admin
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/admin-password-reset-status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Check if the logged in account has an admin reset password request.", description = "This endpoint allows to check whether the account has a password reset request made by an admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request status was read correctly."),
            @ApiResponse(responseCode = "400", description = "Account which made the check doesn't exist."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getPasswordAdminResetStatus() throws ApplicationBaseException;

    /**
     * This method retrieves user account's history data from the system. In order to avoid sending huge amounts of user data
     * it used pagination, so that history data from a particular page, of a particular size can be retrieved.
     * This method retrieves account history data of the account that requests it. The results are ordered by
     * modification time.
     *
     * @param pageNumber Number of the page, which user history data will be retrieved from.
     * @param pageSize   Number of user history data per page.
     * @return This method returns 200 OK as a response, where in response body a list of user account's history data is located, is a JSON format.
     * If the list is empty (there are not user accounts in the system), this method would return 204 NO CONTENT as the response.
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/history-data/self", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get history data of the requesting account", description = "The endpoint is used retrieve list of account history data for the requesting account from given page of given size.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of account's history data returned from given page of given size is not empty."),
            @ApiResponse(responseCode = "204", description = "List of account's history data returned from given page of given size is empty."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getHistoryDataSelf(@RequestParam("pageNumber") int pageNumber,
                                         @RequestParam("pageSize") int pageSize)
            throws ApplicationBaseException;

    /**
     * This method retrieves user account's history data from the system. In order to avoid sending huge amounts of user data
     * it used pagination, so that history data from a particular page, of a particular size can be retrieved.
     * This method retrieves account history data by the account's id. The results are ordered by
     * modification time.
     *
     * @param id         ID of the account which history data will be retrieved.
     * @param pageNumber Number of the page, which user history data will be retrieved from.
     * @param pageSize   Number of user history data per page.
     * @return This method returns 200 OK as a response, where in response body a list of user account's history data is located, is a JSON format.
     * If the list is empty (there are not user accounts in the system), this method would return 204 NO CONTENT as the response.
     * 500 INTERNAL SERVER ERROR is returned when other unexpected exception occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/{id}/history-data", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get history data of a account", description = "The endpoint is used retrieve list of account history data from given page of given size.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of account's history data returned from given page of given size is not empty."),
            @ApiResponse(responseCode = "204", description = "List of account's history data returned from given page of given size is empty."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> getHistoryDataByAccountId(@PathVariable("id") String id,
                                                @RequestParam("pageNumber") int pageNumber,
                                                @RequestParam("pageSize") int pageSize)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to retrieve all attribute names in the dynamic dictionary.
     *
     * @param pageNumber Number of the page with the attribute names.
     * @param pageSize   Size of a single page with the attribute names.
     * @return List of attribute names in the dynamic dictionary on given page with given size with status code 200 OK.
     * If no attribute names were found then the empty list is returned with status code 204 NO CONTENT. 500
     * INTERNAL SERVER ERROR is returned when other, unexpected error occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/attributes", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllAttributesNames(@RequestParam("pageNumber") int pageNumber,
                                            @RequestParam("pageSize") int pageSize)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to retrieve all values for given attribute in the dynamic dictionary.
     *
     * @param attributeName Name of the attribute, which values are to be retrieved.
     * @param pageNumber    Number of the page, which contains attribute values for a given attribute.
     * @param pageSize      Size of the page with attribute values.
     * @return List of attribute values, retrieved for given attribute name, with status code 200 OK.
     * If no values for given attribute name were found, then empty list is returned with status code 204 NO CONTENT.
     * 500 INTERNAL SERVER ERROR is returned when other, unexpected error occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/attributes/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllAttributeValues(@PathVariable("name") String attributeName,
                                            @RequestParam("pageNumber") int pageNumber,
                                            @RequestParam("pageSize") int pageSize)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to add attribute with given name to the dynamic dictionary.
     *
     * @param attributeName Name of the attribute to be added to the dynamic dictionary.
     * @return 204 NO CONTENT is returned when the attribute is created successfully in the dynamic dictionary. 500
     * INTERNAL SERVER ERROR is returned when other, unexpected error occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/attributes/add-attribute/{attributeName}")
    ResponseEntity<?> addAttribute(@PathVariable("attributeName") String attributeName)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to delete attribute with given name from the dynamic dictionary.
     *
     * @param attributeName Name of the attribute to be deleted from the dynamic dictionary.
     * @return HTTP response with status code 204 NO CONTENT is returned when the attribute is deleted successfully
     * from the dynamic dictionary. 500 INTERNAL SERVER ERROR is returned when other, unexpected error occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @DeleteMapping(value = "/attributes/remove-attribute/{attributeName}")
    ResponseEntity<?> removeAttribute(@PathVariable("attributeName") String attributeName)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to add value for attribute with given name in the dynamic dictionary.
     *
     * @param attributeName  Name of the attribute, which value will be added to the dynamic dictionary.
     * @param attributeValue Value of the attribute.
     * @return HTTP response with status code 204 NO CONTENT is returned when the value for given attribute
     * is created in the dynamic dictionary successfully. 500 INTERNAL SERVER ERROR is returned when
     * other, unexpected error occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/attributes/add-value/{attributeName}/{attributeValue}")
    ResponseEntity<?> addAttributeValue(@PathVariable("attributeName") String attributeName,
                                        @PathVariable("attributeValue") String attributeValue)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to remove value for an attribute with given name from the dynamic dictionary.
     *
     * @param attributeName  Name of the attribute, which the value will be removed from the dynamic dictionary for.
     * @param attributeValue Value of the attribute, which will be removed from the dynamic dictionary.
     * @return 204 NO CONTENT HTTP response is returned when the value is removed from the dynamic dictionary
     * successfully. 500 INTERNAL SERVER ERROR is returned when other, unexpected error occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @DeleteMapping(value = "/attributes/remove-value/{attributeName}/{attributeValue}")
    ResponseEntity<?> removeAttributeValue(@PathVariable("attributeName") String attributeName,
                                           @PathVariable("attributeValue") String attributeValue)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to retrieve all attribute name - attribute value pairs for given user account (
     * the one that is currently logged in).
     *
     * @return 200 OK HTTP response is returned with a list of attribute name - value pairs for currently logged-in
     * user account. If there are no attribute mappings for the user account then 204 NO CONTENT is returned. 500
     * INTERNAL SERVER ERROR is returned when other, unexpected error occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @GetMapping(value = "/attributes/account/me/get", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getAllAccountAttributes() throws ApplicationBaseException;

    /**
     * This method is used add new attribute name - value pair for given user account - in this case the one that is
     * currently logged in.
     *
     * @param attributeName  Name of the attribute to be added for user account.
     * @param attributeValue Value of the assigned attribute.
     * @return 204 NO CONTENT is returned when adding new attribute name - value pair for user account was successful.
     * 500 INTERNAL SERVER ERROR is returned when other, unexpected error occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @PostMapping(value = "/attributes/account/me/assign/{attributeName}/{attributeValue}")
    ResponseEntity<?> assignAttribute(@PathVariable("attributeName") String attributeName,
                                      @PathVariable("attributeValue") String attributeValue)
            throws ApplicationBaseException;

    /**
     * This endpoint is used to delete certain attribute name - value mapping for user account - in this case it is the
     * account of the currently logged-in user.
     *
     * @param attributeName Name of the attribute, which the pairing with value will be removed from the dynamic dictionary.
     * @return 204 NO CONTENT is returned when removing certain attribute name - value pair for user account was successful.
     * 500 INTERNAL SERVER ERROR is returned when other, unexpected error occurs.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @DeleteMapping(value = "/attributes/account/me/remove/{attributeName}")
    ResponseEntity<?> removeAttributeValue(@PathVariable("attributeName") String attributeName)
            throws ApplicationBaseException;
}
