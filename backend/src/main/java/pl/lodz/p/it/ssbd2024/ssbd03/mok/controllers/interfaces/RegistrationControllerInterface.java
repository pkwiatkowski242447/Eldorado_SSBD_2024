package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountInputDTO.AccountRegisterDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing registration.
 */
public interface RegistrationControllerInterface {

    /**
     * This endpoint allows both user with administrative user level and with anonymous access to create new account with client user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @PostMapping(value = "/client", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register client", description = "Register new user account with client user level, and send account activation e-mail message to given e-mail address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "New user account with client user level was created successfully and account activation message was sent."),
            @ApiResponse(responseCode = "400", description = "New user account with given data could not be created."),
            @ApiResponse(responseCode = "409", description = "User account with given login or password already exists."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> registerClient(@RequestBody AccountRegisterDTO accountRegisterDTO)
            throws ApplicationBaseException;

    /**
     * This endpoint allows user with administrative user level to create new account with staff user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @PostMapping(value = "/staff", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register staff", description = "Register new user account with staff user level, and send account activation e-mail message to given e-mail address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "New user account with staff user level was created successfully and account activation message was sent."),
            @ApiResponse(responseCode = "400", description = "New user account with given data could not be created."),
            @ApiResponse(responseCode = "409", description = "User account with given login or password already exists."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> registerStaff(@RequestBody AccountRegisterDTO accountRegisterDTO)
            throws ApplicationBaseException;

    /**
     * This endpoint allows user with administrative user level to create new account with admin user level. After
     * the account has been created, the activation link is sent to the e-mail address, specified in the accountRegisterDTO.
     *
     * @param accountRegisterDTO Data transfer object, containing user account data, such as login, password, first name, last name, email and so on.
     * @return If account registration is successful, then 204 NO CONTENT is returned as a response. In case of Persistence exception being thrown
     * during create operation of AccountFacade, AccountCreationException is thrown, which results in 400 BAD REQUEST, with message explaining the problem.
     * If any other exception is thrown, then 400 BAD REQUEST is returned without any additional information.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @PostMapping(value = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register admin", description = "Register new user account with admin user level, and send account activation e-mail message to given e-mail address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "New user account with admin user level was created successfully and account activation message was sent."),
            @ApiResponse(responseCode = "400", description = "New user account with given data could not be created."),
            @ApiResponse(responseCode = "409", description = "User account with given login or password already exists."),
            @ApiResponse(responseCode = "500", description = "Unknown error occurred while the request was being processed.")
    })
    ResponseEntity<?> registerAdmin(@RequestBody AccountRegisterDTO accountRegisterDTO)
            throws ApplicationBaseException;
}
