package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication;

/**
 * Used to specify an Exception related with incorrect credentials during authentication.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AuthenticationInvalidCredentialsException extends AuthenticationBaseException {
    public AuthenticationInvalidCredentialsException(String message) {
        super(message);
    }

    public AuthenticationInvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
