package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountChangePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.integrity.AccountDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request.InvalidRequestHeaderIfMatchException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.InvalidDataFormatException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.AccountControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AccountServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.util.List;
import java.util.UUID;

/**
 * Controller used for manipulating user accounts in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController implements AccountControllerInterface {

    /**
     * AccountServiceInterface used for operation on accounts.
     */
    private final AccountServiceInterface accountService;

    /**
     * JWTProvider used for operations on JWT TOKEN.
     */
    private final JWTProvider jwtProvider;

    /**
     * Autowired constructor for the controller.
     * It is basically used to perform dependency injection of AccountService into this controller.
     *
     * @param accountService Service containing various methods for account manipulation.
     * @param jwtProvider    Service used for JWT management (eg. signing).
     */
    @Autowired
    public AccountController(AccountServiceInterface accountService,
                             JWTProvider jwtProvider) {
        this.accountService = accountService;
        this.jwtProvider = jwtProvider;
    }

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
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/{id}/block", produces = MediaType.TEXT_PLAIN_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Block user account", description = "The endpoint is used to block user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The account has been blocked correctly."),
            @ApiResponse(responseCode = "400", description = "The account has not been blocked due to the correctness of the request or because the account is not available in the database"),
            @ApiResponse(responseCode = "409", description = "The account has not been blocked due to being blocked already or trying to block own account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> blockAccount(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null &&
                    SecurityContextHolder.getContext().getAuthentication().getName()
                            .equals(accountService.getAccountById(UUID.fromString(id)).getLogin())) {
                throw new IllegalOperationException(I18n.ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION);
            }

            accountService.blockAccount(UUID.fromString(id));
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint allows user with administrative user level to unblock a user account by its UUID. After that
     * the user account is unblocked, and it could authenticate in the application.
     *
     * @param id UUID of an Account to unblock.
     * @return If account unblocking is successful, then 204 NO CONTENT is returned as a response.
     * In case of IllegalArgumentException or AccountNotFoundException being thrown, during parsing passed id from String to UUID class,
     * 400 BAD REQUEST is returned, with appropriate message. If AccountAlreadyUnblockedException is thrown, the response is 409 CONFLICT.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/{id}/unblock", produces = MediaType.TEXT_PLAIN_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Unblock user account", description = "The endpoint is used to unblock user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The account has been unblocked correctly."),
            @ApiResponse(responseCode = "400", description = "The account has not been unblocked due to the correctness of the request or because the account is not available in the database"),
            @ApiResponse(responseCode = "409", description = "The account has not been unblocked due to being blocked already."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> unblockAccount(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            accountService.unblockAccount(UUID.fromString(id));
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to "forget" password for an unauthenticated user. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param accountEmailDTO Data transfer object containing unauthenticated user e-mail address, used for registration
     *                        to the application or changed later to other e-mail address.
     * @return 204 NO CONTENT if entire process of forgetting password is successful. Otherwise, 404 NOT FOUND could be returned
     * (if there is no account with given e-mail address) or 400 BAD REQUEST (when account is either blocked or
     * not activated yet).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/forgot-password")
    @RolesAllowed({Roles.ANONYMOUS})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Forget currently set password", description = "The endpoint is used to forget current password, that is to send e-mail message with password reset URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The password change URL has been sent to the user's e-mail address."),
            @ApiResponse(responseCode = "400", description = "The account is blocked or not activated or account with given e-mail has not been found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> forgetAccountPassword(@RequestBody AccountEmailDTO accountEmailDTO) throws ApplicationBaseException {
        try {
            accountService.forgetAccountPassword(accountEmailDTO.getEmail());
        } catch (AccountNotFoundException | AccountNotActivatedException exception) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used to reset user account password by the administrator. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param id Identifier of the account of which the password will be reset.
     * @return 204 NO CONTENT if entire process of resetting password is successful. Otherwise, 404 NOT FOUND could be returned
     * (if there is no account with given e-mail address) or 400 BAD REQUEST (when account is either blocked or
     * not activated yet).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/reset-password/{id}")
    @RolesAllowed({Roles.ADMIN})
    @TxTracked
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Reset account password by admin", description = "The endpoint is used by admin to send password change URL to e-mail address attached to the account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The password change URL has been sent to the user's e-mail address."),
            @ApiResponse(responseCode = "400", description = "The account is blocked or not activated or account with given identifier has not been found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> resetAccountPassword(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            UUID uuid = UUID.fromString(id);
            Account account = accountService.getAccountById(uuid);
            this.accountService.forgetAccountPassword(account.getEmail());

            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException accountNotFoundException) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
    }

    /**
     * This endpoint is used to change password for an unauthenticated user. It does generate RESET PASSWORD token, write
     * it to the database, and send a message with reset password URL to user e-mail address.
     *
     * @param token RESET PASSWORD token required to change password for user account, that was generated when
     *              forgetAccountPassword() method was called.
     * @return 200 OK is returned when changing password goes flawlessly. Otherwise, 400 BAD REQUEST is returned (since
     * RESET PASSWORD token is no longer valid or not in the database).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/change-password/{token_id}")
    @RolesAllowed({Roles.ANONYMOUS})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Change account password", description = "The endpoint is used by unauthenticated user to change their account password, by clicking the link sent to their e-mail address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The password was changed successfully."),
            @ApiResponse(responseCode = "400", description = "The account is blocked or not activated or account, which the password is changed for has not been found, or there is no password change token for given account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> changeAccountPassword(@PathVariable("token_id") String token, @RequestBody AccountPasswordDTO accountPasswordDTO) throws ApplicationBaseException {
        this.accountService.changeAccountPassword(token, accountPasswordDTO.getPassword());
        return ResponseEntity.noContent().build();
    }

    /**
     * This method retrieves user accounts from the system. In order to avoid sending huge amounts of user data
     * it used pagination, so that user accounts from a particular page, of a particular size can be retrieved.
     *
     * @param pageNumber Number of the page, which user accounts will be retrieved from.
     * @param pageSize   Number of user accounts per page.
     * @return This method returns 200 OK as a response, where in response body a list of user accounts is located, is a JSON format.
     * If the list is empty (there are not user accounts in the system), this method would return 204 NO CONTENT as the response.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     * @note. This method retrieves all users accounts, not taking into consideration their role. The results are ordered by
     * login alphabetically.
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    @Operation(summary = "Get all users", description = "The endpoint is used retrieve list of accounts from given page of given size.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of accounts returned from given page of given size is not empty."),
            @ApiResponse(responseCode = "204", description = "List of accounts returned from given page of given size is empty."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> getAllUsers(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize)
            throws ApplicationBaseException {
        List<AccountListDTO> accountList = accountService.getAllAccounts(pageNumber, pageSize)
                .stream()
                .map(AccountListMapper::toAccountListDTO)
                .toList();
        if (accountList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(accountList);
    }

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
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @GetMapping(value = "/match-login-firstname-and-lastname", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    @Operation(summary = "Get all users matching criteria", description = "The endpoint is used retrieve list of accounts that match certain criteria, that is either contain certain phrase in login, firstName, lastName, with certain activity status and ordered by login alphabetically or not.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of accounts returned from given page of given size is not empty."),
            @ApiResponse(responseCode = "204", description = "List of accounts returned from given page of given size is empty."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> getAccountsByMatchingLoginFirstNameAndLastName(@RequestParam(name = "login", defaultValue = "") String login,
                                                                            @RequestParam(name = "firstName", defaultValue = "") String firstName,
                                                                            @RequestParam(name = "lastName", defaultValue = "") String lastName,
                                                                            @RequestParam(name = "active", defaultValue = "true") boolean active,
                                                                            @RequestParam(name = "order", defaultValue = "true") boolean order,
                                                                            @RequestParam(name = "pageNumber") int pageNumber,
                                                                            @RequestParam(name = "pageSize") int pageSize) throws ApplicationBaseException {
        List<AccountListDTO> accountList = accountService.getAccountsByMatchingLoginFirstNameAndLastName(login, firstName,
                        lastName, active, order, pageNumber, pageSize)
                .stream()
                .map(AccountListMapper::toAccountListDTO)
                .toList();
        if (accountList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(accountList);
    }

    /**
     * This method is used to activate user account, after it was successfully
     * registered either by user itself or by user with administrative privileges.
     *
     * @param token Last part of the activation URL, sent to the e-mail address user specified during registration process. It is a JWT token
     *              generated with payload taken from the user account (id and login) and is valid for a certain amount of time.
     * @return This function returns 204 NO CONTENT if method finishes successfully (all performed action finish without any errors).
     * It could also return 204 NO CONTENT if the token is not valid.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping("/activate-account/{token}")
    @RolesAllowed({Roles.ANONYMOUS})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Activate account", description = "The endpoint is used activate user account by itself after successful registration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account activation was successful."),
            @ApiResponse(responseCode = "400", description = "Activation URL expired, is invalid or account, which is being activated is not found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> activateAccount(@PathVariable("token") String token) throws ApplicationBaseException {
        if (accountService.activateAccount(token)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * This method is used to confirm the change of an e-mail
     *
     * @param token Last part of the activation URL, sent to the new e-mail address. It is a JWT token
     *              generated with payload taken from the user account (id and login) and is valid for a certain amount of time.
     * @return This function returns 204 NO CONTENT if method finishes successfully.
     * It could also return 400 BAD REQUEST if the token is not valid, expired or account does not exist.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/confirm-email/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ANONYMOUS})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Confirm e-mail", description = "The endpoint is used confirm e-mail attached to user's account after it was changed by the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "E-mail confirmation process was successful."),
            @ApiResponse(responseCode = "400", description = "E-mail confirmation URL expired, is invalid or account, which is e-mail is activated for, is not found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> confirmEmail(@PathVariable(value = "token") String token) throws ApplicationBaseException {
        if (accountService.confirmEmail(token)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().body(I18n.TOKEN_INVALID_OR_EXPIRED);
        }
    }

    /**
     * This method is used to find user account of currently logged-in user.
     *
     * @return If user account is found for currently logged user then 200 OK with user account in the response
     * body is returned, otherwise 400 BAD REQUEST is returned, since user account could not be found.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @GetMapping(value = "/self", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.AUTHENTICATED})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    @Operation(summary = "Get your account details", description = "The endpoint is used to get user's own account details, and sign them using JWS, where the signature is placed in ETag header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's accounts details were found successfully."),
            @ApiResponse(responseCode = "400", description = "Account of the currently logged in user could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> getSelf() throws ApplicationBaseException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.getAccountByLogin(login);

        AccountOutputDTO accountDTO = AccountMapper.toAccountOutputDto(account);
        HttpHeaders headers = new HttpHeaders();
        headers.setETag(String.format("\"%s\"", jwtProvider.generateObjectSignature(accountDTO)));

        return ResponseEntity.ok().headers(headers).body(accountDTO);
    }

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
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PutMapping(value = "/self", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @RolesAllowed({Roles.AUTHENTICATED})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Modify self account", description = "The endpoint is used to modify self account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The account has been modified correctly.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountOutputDTO.class))),
            @ApiResponse(responseCode = "400", description = "The account has not been modified due to the correctness of the request or because the account is not available in the database", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "409", description = "The account has not been modified due to modification of signed fields.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    public ResponseEntity<?> modifyAccountSelf(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                               @Valid @RequestBody AccountModifyDTO accountModifyDTO) throws ApplicationBaseException {
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new InvalidRequestHeaderIfMatchException();
        }

        if (!ifMatch.equals(jwtProvider.generateObjectSignature(accountModifyDTO))) {
            throw new AccountDataIntegrityCompromisedException();
        }

        //TODO maybe handle null (other methods same)??
        String currentUserLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        AccountOutputDTO accountOutputDTO = AccountMapper.toAccountOutputDto(
                accountService.modifyAccount(AccountMapper.toAccount(accountModifyDTO), currentUserLogin)
        );
        return ResponseEntity.ok().body(accountOutputDTO);
    }

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
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Modify other user account account", description = "The endpoint is used to modify user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The account has been modified correctly.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountOutputDTO.class))),
            @ApiResponse(responseCode = "400", description = "The account has not been modified due to the correctness of the request or because the account is not available in the database", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "409", description = "The account has not been modified due to parallel modification.", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    public ResponseEntity<?> modifyUserAccount(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                               @Valid @RequestBody AccountModifyDTO accountModifyDTO) throws ApplicationBaseException {
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new InvalidRequestHeaderIfMatchException();
        }

        if (!ifMatch.equals(jwtProvider.generateObjectSignature(accountModifyDTO))) {
            throw new AccountDataIntegrityCompromisedException();
        }

        AccountOutputDTO accountOutputDTO = AccountMapper.toAccountOutputDto(
                accountService.modifyAccount(AccountMapper.toAccount(accountModifyDTO), accountModifyDTO.getLogin())
        );
        return ResponseEntity.ok().body(accountOutputDTO);
    }

    /**
     * This method is used to modify personal data of currently logged-in user.
     *
     * @param id Identifier of account to find.
     * @return It returns HTTP response 200 OK with user information if account exists. If Account with id doesn't exist
     * returns 400. When uuid is invalid returns 400.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    @Operation(summary = "Find user account by id", description = "The endpoint is used retrieve user account details by its identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's account details were found successfully."),
            @ApiResponse(responseCode = "400", description = "User account could not be found in the database."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            UUID uuid = UUID.fromString(id);
            Account account = accountService.getAccountById(uuid);

            AccountOutputDTO accountOutputDTO = AccountMapper.toAccountOutputDto(account);
            HttpHeaders headers = new HttpHeaders();
            headers.setETag(String.format("\"%s\"", jwtProvider.generateObjectSignature(accountOutputDTO)));

            return ResponseEntity.ok().headers(headers).body(accountOutputDTO);
        } catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.badRequest().body(I18n.UUID_INVALID);
        } catch (AccountNotFoundException accountNotFoundException) {
            return ResponseEntity.notFound().build();
        }
    }

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
    @Override
    @PatchMapping(value = "/{id}/change-email", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Change other user's e-mail address", description = "The endpoint is used change e-mail address, attached to certain user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "E-mail change confirmation message was successfully sent to the current e-mail address attached to the user's account."),
            @ApiResponse(responseCode = "400", description = "Given e-mail address is already set, is already taken by other user or user account could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> changeEmail(@PathVariable("id") UUID id, @Valid @RequestBody AccountEmailDTO accountEmailDTO) throws ApplicationBaseException {
        accountService.changeEmail(id, accountEmailDTO.getEmail());
        return ResponseEntity.noContent().build();
    }

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
    @Override
    @PatchMapping(value = "/change-email-self", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.AUTHENTICATED})
    @TxTracked
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Change own e-mail address", description = "The endpoint is used change e-mail address, attached to own user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "E-mail change confirmation message was successfully sent to the current e-mail address attached to the user's account."),
            @ApiResponse(responseCode = "400", description = "Given e-mail address is already set, is already taken by other user or user account could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> changeEmailSelf(@Valid @RequestBody AccountEmailDTO accountEmailDTO) throws ApplicationBaseException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.getAccountByLogin(login);
        accountService.changeEmail(user.getId(), accountEmailDTO.getEmail());
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to resend confirmation e-mail message.
     * It generates a new token used in a confirmation.
     *
     * @return This method returns 200 OK if the mail with new e-mail confirmation message was successfully sent.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/resend-email-confirmation")
    @RolesAllowed({Roles.AUTHENTICATED})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Resend e-mail confirmation message", description = "The endpoint is used resend e-mail confirmation message, that would be used to change e-mail address attached to user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "E-mail change confirmation message is sent successfully."),
            @ApiResponse(responseCode = "400", description = "There is no token for account's e-mail change or the account, which the e-mail is change for, could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> resendEmailConfirmation() throws ApplicationBaseException {
        accountService.resendEmailConfirmation();
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to remove client user level from account.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 400 BAD REQUEST is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/{id}/remove-level-client", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Remove client user level", description = "The endpoint is used to remove client user level from account with given identifier, if the account contains such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Client user level was removed successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does not have client user level, or it is the only user level that account has."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> removeClientUserLevel(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            accountService.removeClientUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to remove staff user level from account.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 400 BAD REQUEST is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/{id}/remove-level-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Remove staff user level", description = "The endpoint is used to remove staff user level from account with given identifier, if the account contains such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Staff user level was removed successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does not have staff user level, or it is the only user level that account has."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> removeStaffUserLevel(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            accountService.removeStaffUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to remove admin user level from account.
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 400 BAD REQUEST is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred).
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/{id}/remove-level-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Remove admin user level", description = "The endpoint is used to remove staff user level from account with given identifier, if the account contains such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Admin user level was removed successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does not have admin user level, or it is the only user level that account has, or it is the user level of the currently logged in user."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> removeAdminUserLevel(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            accountService.removeAdminUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

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
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/{id}/add-level-client", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Add client user level", description = "The endpoint is used to add client user level to the account with given identifier, if the account does not have such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Client user level was added to the account successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does already have client user level, or all user levels are assigned to the account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> addClientUserLevel(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            accountService.addClientUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

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
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/{id}/add-level-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Add staff user level", description = "The endpoint is used to add staff user level to the account with given identifier, if the account does not have such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Staff user level was added to the account successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does already have staff user level, or all user levels are assigned to the account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> addStaffUserLevel(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            accountService.addStaffUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

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
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PostMapping(value = "/{id}/add-level-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.ADMIN})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Add admin user level", description = "The endpoint is used to add admin user level to the account with given identifier, if the account does not have such a user level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204 ", description = "Admin user level was added to the account successfully."),
            @ApiResponse(responseCode = "400", description = "Account with given id does already have admin user level, or all user levels are assigned to the account."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> addAdminUserLevel(@PathVariable("id") String id) throws ApplicationBaseException {
        try {
            accountService.addAdminUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * This method is used to change own password.
     *
     * @param accountChangePasswordDTO Data transfer object containing old Password and new password.
     * @return If password successfully changed returns 200 OK Http response. If old password is incorrect or new password
     * is the same as current password returns 400 BAD REQUEST HTTP response.
     * @throws ApplicationBaseException General superclass for all exceptions thrown in this method or handled by
     *                                  exception handling aspects from facade and service layers below.
     */
    @Override
    @PatchMapping(value = "/change-password/self", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Roles.AUTHENTICATED})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    @Operation(summary = "Change own password", description = "The endpoint is used to change password, used to authenticate to the currently logged in account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 ", description = "Account's password was changed successfully."),
            @ApiResponse(responseCode = "400", description = "Given past password is not the same as the old one, new password is the same as the old one or account, which the password is change for could not be found."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    public ResponseEntity<?> changePasswordSelf(@RequestBody AccountChangePasswordDTO accountChangePasswordDTO) throws ApplicationBaseException {
        String oldPassword = accountChangePasswordDTO.getOldPassword();
        String newPassword = accountChangePasswordDTO.getNewPassword();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        accountService.changePasswordSelf(oldPassword, newPassword, username);

        return ResponseEntity.ok().build();
    }
}
