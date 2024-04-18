package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.authentication;

public class AuthenticationAccountNotFoundException extends AuthenticationException {
    public AuthenticationAccountNotFoundException(String message) {
        super(message);
    }

    public AuthenticationAccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
