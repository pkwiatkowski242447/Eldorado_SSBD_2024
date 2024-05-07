package pl.lodz.p.it.ssbd2024.ssbd03.exceptions;

public class ApplicationBaseException extends Exception{

    public ApplicationBaseException() {
    }

    public ApplicationBaseException(String message) {
        super(message);
    }

    public ApplicationBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationBaseException(Throwable cause) {
        super(cause);
    }
}
