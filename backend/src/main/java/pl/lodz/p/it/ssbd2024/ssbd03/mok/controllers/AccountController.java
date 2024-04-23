package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AccountService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
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
}
