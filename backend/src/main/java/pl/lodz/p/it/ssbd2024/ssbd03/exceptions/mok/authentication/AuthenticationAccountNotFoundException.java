package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.authentication;

/**
 * Used to specify an Exception related with finding an Account during authentication.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account
 */
public class AuthenticationAccountNotFoundException extends AuthenticationBaseException {
    public AuthenticationAccountNotFoundException(String message) {
        super(message);
    }

    public AuthenticationAccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
