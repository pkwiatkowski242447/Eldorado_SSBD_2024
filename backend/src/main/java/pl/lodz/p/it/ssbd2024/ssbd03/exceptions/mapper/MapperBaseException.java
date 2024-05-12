package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * Used to specify an Exception related with mapping entity-DTO.
 */
public class MapperBaseException extends ApplicationBaseException {

    public MapperBaseException() {
    }

    public MapperBaseException(String message) {
        super(message);
    }

    public MapperBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperBaseException(Throwable cause) {
        super(cause);
    }
}
