package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountCreationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.AccountService;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.TokenService;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/register")
public class RegistrationController {

    private final AccountService accountService;
    private final TokenService tokenService;
    private final MailProvider mailProvider;

    @Autowired
    public RegistrationController(AccountService accountService,
                                  TokenService tokenService,
                                  MailProvider mailProvider) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.mailProvider = mailProvider;
    }

    @PostMapping( value = "/client", consumes = MediaType.APPLICATION_JSON_VALUE)
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
            mailProvider.sendRegistrationConfirmEmail(accountRegisterDTO.getFirstName(), accountRegisterDTO.getLastName(), accountRegisterDTO.getEmail(), confirmationURL, accountRegisterDTO.getLanguage());
            return ResponseEntity.noContent().build();
        } catch (AccountCreationException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * This endpoint allows user with administrative user level to create new account with staff user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     */
    @PreAuthorize(value = "hasRole(T(pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts).ADMIN_DISCRIMINATOR)")
    @PostMapping(value = "/staff", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerStaff(@RequestBody AccountRegisterDTO accountRegisterDTO) {
        try {
            this.accountService.registerStaff(accountRegisterDTO.getLogin(),
                    accountRegisterDTO.getPassword(),
                    accountRegisterDTO.getFirstName(),
                    accountRegisterDTO.getLastName(),
                    accountRegisterDTO.getEmail(),
                    accountRegisterDTO.getPhoneNumber(),
                    accountRegisterDTO.getLanguage());
            return ResponseEntity.noContent().build();
        } catch (AccountCreationException exception) {
            log.error(exception.getMessage(), exception);
            return ResponseEntity.badRequest().body(I18n.getMessage(exception.getMessage(), accountRegisterDTO.getLanguage()));
        } catch (Throwable exception) {
            log.error(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * This endpoint allows user with administrative user level to create new account with admin user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     */
    @PreAuthorize(value = "hasRole(T(pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts).ADMIN_DISCRIMINATOR)")
    @PostMapping(value = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerAdmin(@RequestBody AccountRegisterDTO accountRegisterDTO) {
        try {
            this.accountService.registerAdmin(accountRegisterDTO.getLogin(),
                    accountRegisterDTO.getPassword(),
                    accountRegisterDTO.getFirstName(),
                    accountRegisterDTO.getLastName(),
                    accountRegisterDTO.getEmail(),
                    accountRegisterDTO.getPhoneNumber(),
                    accountRegisterDTO.getLanguage());
            return ResponseEntity.noContent().build();
        } catch (AccountCreationException exception) {
            log.error(exception.getMessage(), exception);
            return ResponseEntity.badRequest().body(I18n.getMessage(exception.getMessage(), accountRegisterDTO.getLanguage()));
        } catch (Throwable exception) {
            log.error(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();
        }
    }
}
