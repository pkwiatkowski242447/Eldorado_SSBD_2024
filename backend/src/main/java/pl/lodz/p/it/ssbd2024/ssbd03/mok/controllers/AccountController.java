package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountChangeEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountAlreadyBlockedException;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountEmailChangeException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountValidationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.TokenService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller used for manipulating user accounts in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AccountController {

    private final AccountService accountService;
    private final TokenService tokenService;
    private final MailProvider mailProvider;

    /**
     * Autowired constructor for the controller.
     * It is basically used to perform dependency injection of AccountService into this controller.
     *
     * @param accountService Service containing various methods for account manipulation.
     */
    @Autowired
    public AccountController(AccountService accountService,
                             TokenService tokenService,
                             MailProvider mailProvider) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.mailProvider = mailProvider;
    }

    /**
     * This endpoint allows user with administrative user level to block a user account by its UUID. After
     * the account has been blocked.
     *
     * @param id
     * @return If account blocking is successful, then 204 NO CONTENT is returned as a response.
     * In case of IllegalArgumentException being thrown, during parsing passed id from String to UUID class,
     * 400 BAD REQUEST is returned, with appropriate message. If AccountNotFoundException is thrown, the response is
     * 404 NOT FOUND and when AccountAlreadyBlockedException or IllegalOperationException is thrown,
     * the response is 409 CONFLICT.
     */
    @PostMapping("/{user_id}/block")
    public ResponseEntity<?> blockAccount(@PathVariable("user_id") String id) {
        try {
            accountService.blockAccount(UUID.fromString(id));
        } catch (IllegalArgumentException iae) {
            log.error(iae.getMessage());
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (AccountNotFoundException ignore) {
            log.error("Account not found");
            ///TODO ewewntualna zmiana kodu z NF na bad request?
            return ResponseEntity.notFound().build();
        } catch (AccountAlreadyBlockedException | IllegalOperationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
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
    public ResponseEntity<?> confirmEmail(@PathVariable(value = "token") String token, @RequestBody Map<String,String> language) {
        try {
            if (accountService.confirmEmail(token)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().body(I18n.getMessage(I18n.TOKEN_INVALID_OR_EXPIRED, language.get("language")));
            }
        } catch (AccountNotFoundException e) {
            return ResponseEntity.badRequest().body(I18n.getMessage(e.getMessage(), language.get("language")));
        }
    }

    /**
     * This method is used to find user account of currently logged in user.
     *
     * @return If user account is found for currently logged user then 200 OK with user account in the response
     * body is returned, otherwise 500 INTERNAL SERVER ERROR is returned, since user account could not be found.
     */
    @GetMapping(value = "/self", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSelf() {
        //getUserLoginFromSecurityContextHolder
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //call accountServiceMethod [findByLogin()]
        Account account = accountService.getAccountByLogin(username);
        if (account == null) {
            return ResponseEntity.internalServerError().body(I18n.getMessage(I18n.ACCOUNT_NOT_FOUND_ACCOUNT_CONTROLLER, "en"));
        } else {
            return ResponseEntity.ok(AccountMapper.toAccountOutputDto(account));
        }
    }

    /**
     * This method is used to change users e-mail address, which later could be used to send
     * messages about user actions in the application (e.g. messages containing confirmation links).
     *
     * @param id Identifier of the user account, whose e-mail will be changed by this method.
     * @param accountChangeEmailDTO Data transfer object containing new e-mail address.
     *
     * @return If changing e-mail address is successful, then 204 NO CONTENT is returned. Otherwise, if user account
     * could not be found (and therefore e-mail address could not be changed) then 404 NOT FOUND is returned. If account
     * is found but new e-mail does not follow constraints, then 500 INTERNAL SERVER ERROR is returned (with a message
     * explaining why the error occurred).
     */
    @PatchMapping(value = "/{id}/change-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeEmail(@PathVariable("id") UUID id, @RequestBody AccountChangeEmailDTO accountChangeEmailDTO) {
        try {
            Account account = accountService.getAccountById(id).orElseThrow(AccountNotFoundException::new);
            accountService.changeEmail(account, accountChangeEmailDTO.getEmail());
            var token = tokenService.createEmailConfirmationToken(account);

            //TODO make it so the URL is based on some property
            String confirmationURL = "http://localhost:8080/api/v1/account/change-email/" + token;
            mailProvider.sendEmailConfirmEmail(account.getName(), account.getLastname(), account.getEmail(), confirmationURL, account.getAccountLanguage());

            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (AccountEmailChangeException e) {
            //TODO improve error message to comply with RFC 9457
            log.error(e.getMessage());
            var response = ResponseEntity.badRequest();
            if (e.getCause() instanceof AccountValidationException)
                return response.body(((AccountValidationException) e.getCause()).getConstraintViolations());
            //TODO change to use user's language
            return response.body(I18n.getMessage(e.getMessage(), "en"));
        } catch (Throwable e) {
            log.error(e.getMessage());
            //TODO change to use user's language
            return ResponseEntity.internalServerError().body(I18n.getMessage(e.getMessage(), "en"));
        }
    }
}
