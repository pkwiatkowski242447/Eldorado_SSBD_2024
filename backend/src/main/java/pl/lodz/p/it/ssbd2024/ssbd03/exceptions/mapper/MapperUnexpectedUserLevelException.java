package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper;

public class MapperUnexpectedUserLevelException extends MapperBaseException {

    public MapperUnexpectedUserLevelException() {
    }

    public MapperUnexpectedUserLevelException(String message) {
        super(message);
    }

    public MapperUnexpectedUserLevelException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperUnexpectedUserLevelException(Throwable cause) {
        super(cause);
    }
}
