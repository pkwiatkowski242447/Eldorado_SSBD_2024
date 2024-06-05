package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

public class SectorBaseException extends ApplicationBaseException {

    public SectorBaseException() {}

    public SectorBaseException(String message) {
        super(message);
    }

    public SectorBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SectorBaseException(Throwable cause) {
        super(cause);
    }
}
