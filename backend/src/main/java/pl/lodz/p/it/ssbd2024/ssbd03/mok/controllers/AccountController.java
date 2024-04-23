package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AccountService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize) {
        List<AccountListDTO> accountList = accountService.getAllAccounts(pageNumber, pageSize)
                .stream()
                .map(AccountListMapper::toAccountListDTO)
                .toList();
        if (accountList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(accountList);
    }

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

    @PostMapping("/activate-account/{token}")
    public ResponseEntity<?> activateAccount(@PathVariable(value = "token") String token) {
        if (accountService.activateAccount(token)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/self", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSelf() {
        //getUserLoginFromSecurityContextHolder
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //call accountServiceMethod [findByLogin()]
        Account account =  accountService.getAccountByLogin(username);
        if (account == null) {
            log.info("NIE ZNALAZLEM");
            return ResponseEntity.internalServerError().body("Wystapil blad");
        } else {
            log.info("ZNALAZLEM");
            log.info(account.toString());
            return ResponseEntity.ok(AccountMapper.toAccountOutputDto(account));
        }
    }
}
