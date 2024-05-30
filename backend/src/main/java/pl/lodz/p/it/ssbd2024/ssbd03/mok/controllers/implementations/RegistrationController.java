package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.RegistrationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AccountServiceInterface;

import java.net.URI;

/**
 * Controller used to create new Accounts in the system.
 *
 * @see Account
 */
@Slf4j
@RestController
@LoggerInterceptor
@RequestMapping(value = "/api/v1/register")
@Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
        retryFor = {ApplicationDatabaseException.class, RollbackException.class})
public class RegistrationController implements RegistrationControllerInterface {

    @Value("${created.account.resource.url}")
    private String createdAccountResourceURL;

    /**
     * AccountServiceInterface used for operation on accounts.
     */
    private final AccountServiceInterface accountService;

    /**
     * Autowired constructor for the controller.
     *
     * @param accountService Service containing method for account manipulation.
     */
    @Autowired
    public RegistrationController(AccountServiceInterface accountService) {
        this.accountService = accountService;
    }

    // Register methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Authorities.REGISTER_CLIENT, Authorities.REGISTER_USER})
    public ResponseEntity<?> registerClient(@RequestBody AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException {
        Account clientAccount = this.accountService.registerClient(accountRegisterDTO.getLogin(),
                accountRegisterDTO.getPassword(),
                accountRegisterDTO.getFirstName(),
                accountRegisterDTO.getLastName(),
                accountRegisterDTO.getEmail(),
                accountRegisterDTO.getPhoneNumber(),
                accountRegisterDTO.getLanguage());
        return ResponseEntity.created(URI.create(this.createdAccountResourceURL + clientAccount.getId())).build();
    }

    @Override
    @RolesAllowed({Authorities.REGISTER_USER})
    public ResponseEntity<?> registerStaff(@RequestBody AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException {
        Account staffAccount = this.accountService.registerStaff(accountRegisterDTO.getLogin(),
                accountRegisterDTO.getPassword(),
                accountRegisterDTO.getFirstName(),
                accountRegisterDTO.getLastName(),
                accountRegisterDTO.getEmail(),
                accountRegisterDTO.getPhoneNumber(),
                accountRegisterDTO.getLanguage());
        return ResponseEntity.created(URI.create(this.createdAccountResourceURL + staffAccount.getId())).build();
    }

    @Override
    @RolesAllowed({Authorities.REGISTER_USER})
    public ResponseEntity<?> registerAdmin(@RequestBody AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException {
        Account adminAccount = this.accountService.registerAdmin(accountRegisterDTO.getLogin(),
                accountRegisterDTO.getPassword(),
                accountRegisterDTO.getFirstName(),
                accountRegisterDTO.getLastName(),
                accountRegisterDTO.getEmail(),
                accountRegisterDTO.getPhoneNumber(),
                accountRegisterDTO.getLanguage());
        return ResponseEntity.created(URI.create(this.createdAccountResourceURL + adminAccount.getId())).build();
    }
}
