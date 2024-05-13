package pl.lodz.p.it.ssbd2024.ssbd03.exceptions;

public class ApplicationDatabaseException extends ApplicationBaseException {

    public ApplicationDatabaseException(Throwable cause) {
        super(cause);
    }
    public ApplicationDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
