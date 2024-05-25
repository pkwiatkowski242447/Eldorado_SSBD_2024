package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.RegistrationControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AccountServiceInterface;

/**
 * Controller used to create new Accounts in the system.
 *
 * @see Account
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/register")
@Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
        retryFor = {ApplicationDatabaseException.class, RollbackException.class})
public class RegistrationController implements RegistrationControllerInterface {

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

    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.ADMIN})
    public ResponseEntity<?> registerClient(@RequestBody AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException {
        this.accountService.registerClient(accountRegisterDTO.getLogin(),
                accountRegisterDTO.getPassword(),
                accountRegisterDTO.getFirstName(),
                accountRegisterDTO.getLastName(),
                accountRegisterDTO.getEmail(),
                accountRegisterDTO.getPhoneNumber(),
                accountRegisterDTO.getLanguage());
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    public ResponseEntity<?> registerStaff(@RequestBody AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException {
        this.accountService.registerStaff(accountRegisterDTO.getLogin(),
                accountRegisterDTO.getPassword(),
                accountRegisterDTO.getFirstName(),
                accountRegisterDTO.getLastName(),
                accountRegisterDTO.getEmail(),
                accountRegisterDTO.getPhoneNumber(),
                accountRegisterDTO.getLanguage());
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    public ResponseEntity<?> registerAdmin(@RequestBody AccountRegisterDTO accountRegisterDTO) throws ApplicationBaseException {
        this.accountService.registerAdmin(accountRegisterDTO.getLogin(),
                accountRegisterDTO.getPassword(),
                accountRegisterDTO.getFirstName(),
                accountRegisterDTO.getLastName(),
                accountRegisterDTO.getEmail(),
                accountRegisterDTO.getPhoneNumber(),
                accountRegisterDTO.getLanguage());
        return ResponseEntity.noContent().build();
    }
}
