package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AccountService;

import java.util.List;

/**
 * Controller used for manipulating user accounts in the system.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    /**
     * Autowired constructor for the controller.
     * It is basically used to perform dependency injection of AccountService into this controller.
     *
     * @param accountService Service containing various methods for account manipulation.
     */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * This method retrieves user accounts from the system. In order to avoid sending huge amounts of user data
     * it used pagination, so that users from a particular page, of a particular size can be retrieved.
     *
     * @param pageNumber Number of the page, which user accounts will be retrieved from.
     * @param pageSize   Number of user accounts per page.
     *
     * @return This method returns 200 OK as a response, where in response body a list of user accounts is located, is a JSON format.
     * If the list is empty (there are not user accounts in the system), this method would return 204 NO CONTENT as the response.
     *
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
     * @param login         Login of the searched user account. Its default value is empty string (in that case this parameter will not have any impact of final result of the search).
     * @param firstName     First name of the searched users.
     * @param lastName      Last name of the searched users.
     * @param order         Ordering of the searched users. Could be either true (for ascending order) or false (for descending order).
     * @param pageNumber    Number of the page containing searched users.
     * @param pageSize      Number of the users per page.
     *
     * @return This method returns 200 OK response, with list of users in the response body, converted to JSON.
     * If the list is empty, then 204 NO CONTENT is returned.
     */
    @GetMapping("/match-login-firstname-and-lastname")
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
     *
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
}
