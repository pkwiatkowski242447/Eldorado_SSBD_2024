package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.RollbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.AccountRegisterDTO;
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
        retryFor = { ApplicationDatabaseException.class, RollbackException.class })
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

    /**
     * This endpoint allows both user with administrative user level and with anonymous access to create new account with client user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     */
    @Override
    @PostMapping(value = "/client", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register client", description = "Register new user account with client user level, and send account activation e-mail message to given e-mail address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "New user account with client user level was created successfully and account activation message was sent."),
            @ApiResponse(responseCode = "400", description = "New user account with given data could not be created."),
            @ApiResponse(responseCode = "409", description = "User account with given login or password already exists."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
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

    /**
     * This endpoint allows user with administrative user level to create new account with staff user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     */
    @Override
    @PreAuthorize(value = "hasRole(T(pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts).ADMIN_DISCRIMINATOR)")
    @PostMapping(value = "/staff", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register staff", description = "Register new user account with staff user level, and send account activation e-mail message to given e-mail address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "New user account with staff user level was created successfully and account activation message was sent."),
            @ApiResponse(responseCode = "400", description = "New user account with given data could not be created."),
            @ApiResponse(responseCode = "409", description = "User account with given login or password already exists."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
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

    /**
     * This endpoint allows user with administrative user level to create new account with admin user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     */
    @Override
    @PreAuthorize(value = "hasRole(T(pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts).ADMIN_DISCRIMINATOR)")
    @PostMapping(value = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register admin", description = "Register new user account with admin user level, and send account activation e-mail message to given e-mail address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "New user account with admin user level was created successfully and account activation message was sent."),
            @ApiResponse(responseCode = "400", description = "New user account with given data could not be created."),
            @ApiResponse(responseCode = "409", description = "User account with given login or password already exists."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
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
