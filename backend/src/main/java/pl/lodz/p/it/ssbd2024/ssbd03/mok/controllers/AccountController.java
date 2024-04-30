package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountChangeEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication.AuthenticationAccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.TokenService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.LangCodes;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import java.util.List;
import java.util.UUID;

/**
 * Controller used for manipulating user accounts in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final MailProvider mailProvider;
    private final JWTProvider jwtProvider;
    private final TokenService tokenService;
    private final TokenFacade tokenFacade;
    private final AuthenticationService authenticationService;

    /**
     * Autowired constructor for the controller.
     * It is basically used to perform dependency injection of AccountService into this controller.
     *
     * @param accountService Service containing various methods for account manipulation.
     * @param tokenFacade    Facade used in the `resendEmailConfirmation()` method.
     * @param tokenService   Service used in the `changeEmail()` method.
     * @param mailProvider   Component used to send confirmation emails.
     */
    @Autowired
    public AccountController(AccountService accountService,
                             TokenService tokenService,
                             TokenFacade tokenFacade,
                             MailProvider mailProvider,
                             JWTProvider jwtProvider,
                             AuthenticationService authenticationService) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
        this.jwtProvider = jwtProvider;
        this.authenticationService = authenticationService;
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
    @PostMapping(value = "/{user_id}/block", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> blockAccount(@PathVariable("user_id") String id) {
        try {
            if (id.length() != 36) {
                throw new IllegalArgumentException();
            }
            accountService.blockAccount(UUID.fromString(id));
        } catch (IllegalArgumentException iae) {
            log.error(I18n.getMessage(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, LangCodes.EN.getCode()));
            return ResponseEntity.badRequest().body(I18n.getMessage(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, getSelfAccountLang()));
        } catch (AccountNotFoundException anfe) {
            log.error(I18n.getMessage(anfe.getMessage(), LangCodes.EN.getCode()));
            ///TODO potentially change from NF to bad request?
            ///FIXME throwning Internal Error - Unexpected rollback - interceptor will fix that?
            return ResponseEntity.notFound().build();
        } catch (AccountAlreadyBlockedException | IllegalOperationException e) {
            log.error(I18n.getMessage(e.getMessage(), LangCodes.EN.getCode()));
            ///FIXME throwning Internal Error - Unexpected rollback - interceptor will fix that?
            return ResponseEntity.status(HttpStatus.CONFLICT).body(I18n.getMessage(e.getMessage(), getSelfAccountLang()));
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
    @PostMapping(value = "/{user_id}/unblock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> unblockAccount(@PathVariable("user_id") String id) {
        try {
            if (id.length() != 36) {
                throw new IllegalArgumentException();
            }
            accountService.unblockAccount(UUID.fromString(id));
        } catch (IllegalArgumentException iae) {
            log.error(I18n.getMessage(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, LangCodes.EN.getCode()));
            return ResponseEntity.badRequest().body(I18n.getMessage(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION, getSelfAccountLang()));
        } catch (AccountNotFoundException anfe) {
            log.error(I18n.getMessage(anfe.getMessage(), LangCodes.EN.getCode()));
            ///TODO potentially change from NF to bad request?
            ///FIXME throwning Internal Error - Unexpected rollback - interceptor will fix that?
            return ResponseEntity.notFound().build();
        } catch (AccountAlreadyUnblockedException aaue) {
            log.error(I18n.getMessage(aaue.getMessage(), LangCodes.EN.getCode()));
            ///FIXME throwning Internal Error - Unexpected rollback - interceptor will fix that?
            return ResponseEntity.status(HttpStatus.CONFLICT).body(I18n.getMessage(aaue.getMessage(), getSelfAccountLang()));
        }
        return ResponseEntity.noContent().build();
    }

    /// FIXME method is discussed
    private String getSelfAccountLang() {
        Account account;
        try {
            String login = SecurityContextHolder.getContext().getAuthentication().getName();
            account = accountService.getAccountByLogin(login);
        } catch (Throwable e) {
            log.error("Error getting account by login from the context");
            return LangCodes.EN.getCode();
        }
        return account != null ? account.getAccountLanguage() : LangCodes.EN.getCode();
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
    @PostMapping("/activate-account/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable(value = "token") String token) {
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
        } catch (AccountEmailChangeException e){
            tokenService.removeAccountsEmailConfirmationToken(token);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (AccountEmailNullException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * This method is used to find user account of currently logged in user.
     *
     * @return If user account is found for currently logged user then 200 OK with user account in the response
     * body is returned, otherwise 500 INTERNAL SERVER ERROR is returned, since user account could not be found.
     */
    @GetMapping(value = "/self", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSelf(HttpServletResponse response) {
        //getUserLoginFromSecurityContextHolder
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //call accountServiceMethod [findByLogin()]
        Account account = accountService.getAccountByLogin(username);
        if (account == null) {
            return ResponseEntity.internalServerError().body(I18n.ACCOUNT_NOT_FOUND_ACCOUNT_CONTROLLER);
        } else {
            AccountOutputDTO accountDTO = AccountMapper.toAccountOutputDto(account);
            response.setHeader(HttpHeaders.ETAG, jwtProvider.generateSignatureForAccount(accountDTO));
            return ResponseEntity.ok(accountDTO);
        }
    }

    /**
     * This method is used to find user account by Id.
     *
     * @param id Id of searched of the searched account
     */
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
     * @param accountChangeEmailDTO Data transfer object containing new e-mail address.
     * @return If changing e-mail address is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore e-mail address could not be changed) then 404 NOT FOUND is returned. If account
     * is found but new e-mail does not follow constraints, then 500 INTERNAL SERVER ERROR is returned (with a message
     * explaining why the error occurred).
     */
    @PatchMapping(value = "/{id}/change-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeEmail(@PathVariable("id") UUID id, @Valid @RequestBody AccountChangeEmailDTO accountChangeEmailDTO) {
        try {
            accountService.changeEmail(id, accountChangeEmailDTO.getEmail());
            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccountEmailChangeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This method is used to resend confirmation e-mail message, after e-mail was changed to the new one.
     *
     * @param accountLoginDTO Data transfer object, containing user credentials with language setting from the browser.
     * @return This method returns 204 NO CONTENT if the mail with new e-mail confirmation message was successfully sent.
     * Otherwise, it returns 404 NOT FOUND (since user account with specified username could not be found).
     */
    @PostMapping(value = "/resend-email-confirmation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resendEmailConfirmation(@RequestBody AccountLoginDTO accountLoginDTO) {
        try {
            // TODO: Verify credentials
            // FIXME imo it should be accountService ???
            Account account = this.authenticationService.findByLogin(accountLoginDTO.getLogin());
            Token token = this.tokenFacade.findByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL, account.getId()).orElseThrow();
            String confirmationURL = "http://localhost:8080/api/v1/accounts/email/" + token;
            mailProvider.sendRegistrationConfirmEmail(account.getName(),
                    account.getLastname(),
                    account.getEmail(),
                    confirmationURL,
                    account.getAccountLanguage());
            return ResponseEntity.noContent().build();
        } catch (AuthenticationAccountNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(I18n.getMessage(exception.getMessage(), accountLoginDTO.getLanguage()));
        }
    }
}
