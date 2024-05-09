package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyUnblockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.old.AccountEmailChangeException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.old.AccountEmailNullException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.old.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.AccountControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AccountServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.TokenServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.log.AccountLogMessages;
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
     * TokenServiceInterface used for operations on tokens.
     */
    private final TokenServiceInterface tokenService;

    /**
     * Autowired constructor for the controller.
     * It is basically used to perform dependency injection of AccountService into this controller.
     *
     * @param accountService Service containing various methods for account manipulation.
     * @param tokenService   Service used for token management (in order to confirm certain user's actions).
     * @param jwtProvider    Service used for JWT management (eg. signing).
     */
    @Autowired
    public AccountController(AccountServiceInterface accountService,
                             TokenServiceInterface tokenService,
                             JWTProvider jwtProvider) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * This endpoint allows user with administrative user level to block a user account by its UUID. After
     * the account has been blocked.
     *
     * @param id UUID of an Account to block.
     * @return If account blocking is successful, then 204 NO CONTENT is returned as a response.
     * In case of IllegalArgumentException being thrown, during parsing passed id from String to UUID class,
     * 400 BAD REQUEST is returned, with appropriate message. If AccountNotFoundException is thrown, the response is
     * 404 NOT FOUND and when AccountAlreadyBlockedException or IllegalOperationException is thrown,
     * the response is 409 CONFLICT.
     */
    @Override
    @PostMapping(value = "/{user_id}/block", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> blockAccount(@PathVariable("user_id") String id) {
        try {
            if (id.length() != 36) {
                log.error(AccountLogMessages.ACCOUNT_INVALID_UUID_EXCEPTION);
                return ResponseEntity.badRequest().body(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null &&
                    SecurityContextHolder.getContext().getAuthentication().getName()
                            .equals(accountService.getAccountById(UUID.fromString(id)).orElseThrow(
                                    () -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_ACCOUNT_CONTROLLER)
                            ).getLogin())) {
                log.error(I18n.ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION);
                throw new IllegalOperationException(I18n.ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION);
            }

            accountService.blockAccount(UUID.fromString(id));
        } catch (AccountNotFoundException anfe) {
            log.error(AccountLogMessages.ACCOUNT_NOT_FOUND_EXCEPTION);
            return ResponseEntity.badRequest().body(anfe.getMessage());
        } catch (AccountAlreadyBlockedException | IllegalOperationException e) {
            log.error(e instanceof AccountAlreadyBlockedException ?
                    AccountLogMessages.ACCOUNT_ALREADY_BLOCKED_EXCEPTION :
                    AccountLogMessages.ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint allows user with administrative user level to unblock a user account by its UUID. After
     * the account has been unblocked.
     *
     * @param id UUID of an Account to unblock.
     * @return If account unblocking is successful, then 204 NO CONTENT is returned as a response.
     * In case of IllegalArgumentException being thrown, during parsing passed id from String to UUID class,
     * 400 BAD REQUEST is returned, with appropriate message. If AccountNotFoundException is thrown, the response is
     * 404 NOT FOUND and when AccountAlreadyUnblockedException is thrown, the response is 409 CONFLICT.
     */
    @Override
    @PostMapping(value = "/{user_id}/unblock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> unblockAccount(@PathVariable("user_id") String id) {
        try {
            if (id.length() != 36) {
                log.error(AccountLogMessages.ACCOUNT_INVALID_UUID_EXCEPTION);
                return ResponseEntity.badRequest().body(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
            }
            accountService.unblockAccount(UUID.fromString(id));
        } catch (AccountNotFoundException anfe) {
            log.error(AccountLogMessages.ACCOUNT_NOT_FOUND_EXCEPTION);
            return ResponseEntity.badRequest().body(anfe.getMessage());
        } catch (AccountAlreadyUnblockedException aaue) {
            log.error(AccountLogMessages.ACCOUNT_ALREADY_UNBLOCKED_EXCEPTION);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(aaue.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

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
    @Override
    @PostMapping(value = "/forgot-password")
    public ResponseEntity<?> forgetAccountPassword(@RequestBody AccountEmailDTO accountEmailDTO) throws ApplicationBaseException {
        this.accountService.forgetAccountPassword(accountEmailDTO.getEmail());
        return ResponseEntity.noContent().build();
    }

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
    @Override
    @PostMapping(value = "/change-password/{token_id}")
    public ResponseEntity<?> changeAccountPassword(@PathVariable("token_id") String token, @RequestBody AccountPasswordDTO accountPasswordDTO) throws ApplicationBaseException {
        this.accountService.changeAccountPassword(token, accountPasswordDTO.getPassword());
        return ResponseEntity.noContent().build();
    }

    /**
     * This method retrieves user accounts from the system. In order to avoid sending huge amounts of user data
     * it used pagination, so that users from a particular page, of a particular size can be retrieved.
     *
     * @param pageNumber Number of the page, which user accounts will be retrieved from.
     * @param pageSize   Number of user accounts per page.
     * @return This method returns 200 OK as a response, where in response body a list of user accounts is located, is a JSON format.
     * If the list is empty (there are not user accounts in the system), this method would return 204 NO CONTENT as the response.
     * @apiNote This method retrieves all users accounts, not taking into consideration their role. The results are ordered by
     * login alphabetically.
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize) {
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
     */
    @Override
    @GetMapping(value = "/match-login-firstname-and-lastname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountsByMatchingLoginFirstNameAndLastName(@RequestParam(name = "login", defaultValue = "") String login,
                                                                            @RequestParam(name = "firstName", defaultValue = "") String firstName,
                                                                            @RequestParam(name = "lastName", defaultValue = "") String lastName,
                                                                            @RequestParam(name = "order", defaultValue = "true") boolean order,
                                                                            @RequestParam(name = "pageNumber") int pageNumber,
                                                                            @RequestParam(name = "pageSize") int pageSize) {
        List<AccountListDTO> accountList = accountService.getAccountsByMatchingLoginFirstNameAndLastName(login, firstName,
                        lastName, order, pageNumber, pageSize)
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
     */
    @Override
    @PostMapping("/activate-account/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable("token") String token) {
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
     */
    @Override
    @PostMapping(value = "/confirm-email/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirmEmail(@PathVariable(value = "token") String token) {
        try {
            if (accountService.confirmEmail(token)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().body(I18n.TOKEN_INVALID_OR_EXPIRED);
            }
        } catch (AccountNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AccountEmailChangeException e) {
            tokenService.removeAccountsEmailConfirmationToken(token);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AccountEmailNullException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * This method is used to find user account of currently logged-in user.
     *
     * @return If user account is found for currently logged user then 200 OK with user account in the response
     * body is returned, otherwise 500 INTERNAL SERVER ERROR is returned, since user account could not be found.
     */
    @Override
    @GetMapping(value = "/self", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSelf() {
        //getUserLoginFromSecurityContextHolder
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //call accountServiceMethod [findByLogin()]
        Account account = accountService.getAccountByLogin(username);
        if (account == null) {
            return ResponseEntity.internalServerError().body(I18n.ACCOUNT_NOT_FOUND_ACCOUNT_CONTROLLER);
        } else {
            AccountOutputDTO accountDTO = AccountMapper.toAccountOutputDto(account);
            HttpHeaders headers = new HttpHeaders();
            headers.setETag(String.format("\"%s\"", jwtProvider.generateObjectSignature(accountDTO)));
            return ResponseEntity.ok().headers(headers).body(accountDTO);
        }
    }

    /**
     * This method is used to modify personal data of currently logged-in user.
     *
     * @param ifMatch          Value of If-Match header
     * @param accountModifyDTO Account properties with potentially changed values.
     * @return In the correct flow returns account object with applied modifications with 200 OK status.
     * If request has empty IF_MATCH header or account currently logged user was not found,
     * 400 BAD REQUEST is returned. If accountModifyDTO signature is different from IF_MATCH header value
     * then 409 CONFLICT is returned.
     */
    @Override
    @PutMapping(value = "/self", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifySelfAccount(@RequestHeader(HttpHeaders.IF_MATCH) String ifMatch,
                                               @RequestBody AccountModifyDTO accountModifyDTO) {
        if (ifMatch == null || ifMatch.isBlank()) {
            return ResponseEntity.badRequest().body(I18n.MISSING_HEADER_IF_MATCH);
        }

        if (!ifMatch.equals(jwtProvider.generateObjectSignature(accountModifyDTO))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(I18n.DATA_INTEGRITY_COMPROMISED);
        }

        try {
            AccountOutputDTO accountOutputDTO = AccountMapper.toAccountOutputDto(
                    accountService.modifyAccount(AccountMapper.toAccount(accountModifyDTO))
            );
            return ResponseEntity.ok().body(accountOutputDTO);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This method is used to modify personal data of currently logged-in user.
     *
     * @param id Id of account to find.
     * @return It returns HTTP response 200 OK with user information if account exists. If Account with id doesn't exist
     * returns 400. When uuid is invalid returns 400.
     */
    @Override
    @PreAuthorize(value = "hasRole(T(pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts).ADMIN_DISCRIMINATOR)")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        //conversion String -> UUID
        try {
            UUID uuid = UUID.fromString(id);
            Account account = accountService.getAccountById(uuid).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));
            return ResponseEntity.ok(AccountMapper.toAccountOutputDto(account));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(I18n.UUID_INVALID);
        } catch (AccountNotFoundException anfe) {
            return ResponseEntity.badRequest().body(anfe.getMessage());
        }
    }

    /**
     * This method is used to change users e-mail address, which later could be used to send
     * messages about user actions in the application (e.g. messages containing confirmation links).
     *
     * @param id                    Identifier of the user account, whose e-mail will be changed by this method.
     * @param accountEmailDTO Data transfer object containing new e-mail address.
     * @return If changing e-mail address is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore e-mail address could not be changed) then 404 NOT FOUND is returned. If account
     * is found but new e-mail does not follow constraints, then 500 INTERNAL SERVER ERROR is returned (with a message
     * explaining why the error occurred).
     */
    @Override
    @PatchMapping(value = "/{id}/change-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeEmail(@PathVariable("id") UUID id, @Valid @RequestBody AccountEmailDTO accountEmailDTO) {
        try {
            accountService.changeEmail(id, accountEmailDTO.getEmail());
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccountEmailChangeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This method is used to resend confirmation e-mail message.
     * It generates a new token used in a confirmation.
     *
     * @return This method returns 200 OK if the mail with new e-mail confirmation message was successfully sent.
     */
    @Override
    @PostMapping(value = "/resend-email-confirmation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resendEmailConfirmation() {
        try {
            accountService.resendEmailConfirmation();
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException | TokenNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /**
     * This method is used to remove client user level from account.
     *
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred).
     */
    @Override
    @PostMapping(value = "/{id}/remove-level-client", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeClientUserLevel(@PathVariable("id") String id) {
        try {
            accountService.removeClientUserLevel(String.valueOf(UUID.fromString(id)));
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException | AccountUserLevelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This method is used to remove staff user level from account.
     *
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred).
     */
    @Override
    @PostMapping(value = "/{id}/remove-level-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeStaffUserLevel(@PathVariable("id") String id) {
        try {
            accountService.removeStaffUserLevel(String.valueOf(UUID.fromString(id)));
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException | AccountUserLevelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This method is used to remove admin user level from account.
     *
     *
     * @param id Identifier of the user account, whose user level will be changed by this method.
     * @return If removing user level is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore user level could not be changed) then 404 NOT FOUND is returned.
     * If account is found but user level does not follow constraints, then 400 BAD REQUEST is returned (with a message
     * explaining why the error occurred).
     */
    @Override
    @PostMapping(value = "/{id}/remove-level-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeAdminUserLevel(@PathVariable("id") String id) {
        try {
            accountService.removeAdminUserLevel(String.valueOf(UUID.fromString(id)));
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException | AccountUserLevelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
