package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Exception class used for signaling problems while trying to remove
 * sector object from the database, due to the attached reservations.
 */
public class SectorDeleteException extends SectorConflictException {

    public SectorDeleteException() {
        super(I18n.SECTOR_DELETE_EXCEPTION);
    }

    public SectorDeleteException(Throwable cause) {
        super(I18n.SECTOR_DELETE_EXCEPTION, cause);
    }
}
