package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountCreationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.TokenService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

/**
 * Controller used to create new Accounts in the system.
 *
 * @see Account
 */
@RestController
@RequestMapping(value = "/api/v1/register")
public class RegistrationController {

    private final AccountService accountService;
    private final TokenService tokenService;
    private final MailProvider mailProvider;

    /**
     * Autowired constructor for the controller.
     *
     * @param accountService
     * @param tokenService
     * @param mailProvider
     */
    @Autowired
    public RegistrationController(AccountService accountService,
                                  TokenService tokenService,
                                  MailProvider mailProvider) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.mailProvider = mailProvider;
    }

    /**
     * Creates new client and sends account confirmation e-mail.
     *
     * @param accountRegisterDTO New client's details.
     * @return Returns HTTP 204 NO CONTENT, if a problem occurs returns HTTP 400 BAD REQUEST and JSON object containing details about the problem.
     */
    @PostMapping(value = "/client", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<?> registerClient(@RequestBody AccountRegisterDTO accountRegisterDTO) {
        try {
            // Create new account
            Account newAccount = this.accountService.registerClient(accountRegisterDTO.getLogin(),
                    accountRegisterDTO.getPassword(),
                    accountRegisterDTO.getFirstName(),
                    accountRegisterDTO.getLastName(),
                    accountRegisterDTO.getEmail(),
                    accountRegisterDTO.getPhoneNumber(),
                    accountRegisterDTO.getLanguage());
            // Create a corresponding token in the database
            String token = this.tokenService.createRegistrationToken(newAccount);
            // Send a mail with an activation link
            String confirmationURL = "http://localhost:8080/api/v1/account/activate-account/" + token;
            mailProvider.sendRegistrationConfirmEmail(accountRegisterDTO.getFirstName(), accountRegisterDTO.getLastName(), accountRegisterDTO.getEmail(), confirmationURL);
            return ResponseEntity.noContent().build();
        } catch (AccountCreationException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
