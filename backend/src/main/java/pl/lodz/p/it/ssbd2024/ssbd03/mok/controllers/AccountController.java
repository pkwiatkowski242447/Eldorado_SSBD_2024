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
        } catch (AccountAlreadyBlockedException aabe) {
            log.error(aabe.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(aabe.getMessage());
        }
        return ResponseEntity.noContent().build();
    }
}
