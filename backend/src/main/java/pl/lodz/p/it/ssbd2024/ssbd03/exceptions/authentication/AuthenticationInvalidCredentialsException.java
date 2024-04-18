package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication;

public class AuthenticationInvalidCredentialsException extends AuthenticationException {
    public AuthenticationInvalidCredentialsException(String message) {
        super(message);
    }

    public AuthenticationInvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
