package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper;

public class MapperUnexpectedClientTypeException extends MapperBaseException {

    public MapperUnexpectedClientTypeException() {
    }

    public MapperUnexpectedClientTypeException(String message) {
        super(message);
    }

    public MapperUnexpectedClientTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperUnexpectedClientTypeException(Throwable cause) {
        super(cause);
    }
}
