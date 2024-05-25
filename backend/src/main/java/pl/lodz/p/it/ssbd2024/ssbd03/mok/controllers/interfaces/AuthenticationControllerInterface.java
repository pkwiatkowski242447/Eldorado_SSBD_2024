package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.authentication.AuthenticationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.authentication.AuthenticationLoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.token.RefreshTokenDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Interface used for managing Authentication process.
 */
public interface AuthenticationControllerInterface {

    // Login methods

    /**
     * Allows an unauthenticated user to perform the first step in the multifactor authentication,
     * which is to provide credentials to check users identity and for generating the code, which is sent
     * to the users e-mail address.
     *
     * @param accountLoginDTO User's credentials.
     * @param request         HTTP Request in which the credentials.
     * @return In case of successful logging in it returns 200 OK (if the enabled authentication mode is only one factor)
     * or returns HTTP 204 NO CONTENT (in multifactor authentication). When account is blocked or not active
     * then 400 BAD REQUEST is returned. When user credentials are invalid or account is not found 401 UNAUTHORIZED is returned.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @PostMapping(value = "/login-credentials", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enter credentials", description = "This endpoint is used to perform first step in multifactor authentication in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "First step of multifactor authentication was successful. Since user enabled only one factor authentication, then access token is returned."),
            @ApiResponse(responseCode = "204", description = "First step of multifactor authentication was successful. Now enter authentication code for the second step."),
            @ApiResponse(responseCode = "400", description = "User account is blocked, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "401", description = "Given credentials were invalid."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    ResponseEntity<?> loginUsingCredentials(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain,
                                            @Valid @RequestBody AuthenticationLoginDTO accountLoginDTO,
                                            HttpServletRequest request)
            throws ApplicationBaseException;

    /**
     * Allows an unauthenticated user to perform the second step in multifactor authentication,
     * which is to provide authentication code, which was sent to the users e-mail address.
     *
     * @param proxyChain            X-Forwarded-For header content. If set to null source ip address will be extracted from request.
     * @param authenticationCodeDTO Data transfer object containing authentication code.
     * @param request               HTTP Request in which the credentials.
     * @return In case of successful logging in returns HTTP 200 OK is returned.
     * If any problems occur returns HTTP 400 BAD REQUEST with the problem description.
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @PostMapping(value = "/login-auth-code", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enter authentication code", description = "This endpoint is used to perform second step in multifactor authentication in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Full authentication process was successful."),
            @ApiResponse(responseCode = "400", description = "User account is blocked, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "401", description = "Given credentials were invalid."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    ResponseEntity<?> loginUsingAuthenticationCode(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain,
                                                   @RequestBody AuthenticationCodeDTO authenticationCodeDTO,
                                                   HttpServletRequest request)
            throws ApplicationBaseException;

    // Refresh user session method

    /**
     * Allows authenticated user to refresh session in the application, after authentication, performed while logging in.
     * After refreshing session new access token and refresh token are generated.
     *
     * @param proxyChain      Header containing IPv4 address of the user.
     * @param refreshTokenDTO Data transfer object containing refresh token, used for refreshing session of authenticated
     *                        user in the application.
     * @param request         HTTP Request in which the credentials.
     * @return Data transfer object containing access token (used for authentication purposes) and refresh token (for
     * refreshing authenticated user session).
     * @throws ApplicationBaseException Superclass for any application exception thrown by exception handling aspects in the
     *                                  layer of facade and service components in the application.
     */
    @PostMapping(value = "/refresh-session", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Refresh session", description = "This endpoint is used to refresh authenticated user session in the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session was refreshed successfully and new access and refresh token were generated."),
            @ApiResponse(responseCode = "400", description = "User account is blocked or not active, and therefore could not be used authenticated."),
            @ApiResponse(responseCode = "500", description = "Unknown exception occurred during logging attempt.")
    })
    ResponseEntity<?> refreshUserSession(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain,
                                         @Valid @RequestBody RefreshTokenDTO refreshTokenDTO,
                                         HttpServletRequest request)
            throws ApplicationBaseException;

    // Logout method

    /**
     * This method is used to log out from the application, in a situation
     * when user was previously authenticated.
     *
     * @param request  HttpRequest object, associated with the current request.
     * @param response HttpResponse object, .
     * @return 204 OK is returned when user is logged out successfully.
     */
    @PostMapping(value = "/logout")
    @Operation(summary = "Log out", description = "This endpoint is used to log out a user from the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logging out previously authenticated user was successful."),
    })
    ResponseEntity<?> logout(@RequestHeader(value = "X-Forwarded-For", required = false) String proxyChain,
                             HttpServletRequest request,
                             HttpServletResponse response);
}
