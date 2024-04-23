package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication;

/**
 * Basic authentication Exception.
 */
public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
