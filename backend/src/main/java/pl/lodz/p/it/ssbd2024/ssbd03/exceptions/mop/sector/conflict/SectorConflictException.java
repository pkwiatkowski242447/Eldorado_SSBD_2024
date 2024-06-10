package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.SectorBaseException;

/**
 * Exception class used to indicate possible problems with sector
 * in terms of conflict situations, like removing sector with reservations.
 */
public class SectorConflictException extends SectorBaseException {

    public SectorConflictException() {
    }

    public SectorConflictException(String message) {
        super(message);
    }

    public SectorConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public SectorConflictException(Throwable cause) {
        super(cause);
    }
}
