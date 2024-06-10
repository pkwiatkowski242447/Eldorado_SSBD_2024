package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Exception used to indicate that there is already a sector with given name defined
 * for the parking entity object.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector
 */
public class ParkingSectorNameDuplicateException extends ParkingConflictException {

    public ParkingSectorNameDuplicateException() {
        super(I18n.SECTOR_WITH_GIVEN_NAME_EXISTS_EXCEPTION);
    }

    public ParkingSectorNameDuplicateException(Throwable cause) {
        super(I18n.SECTOR_WITH_GIVEN_NAME_EXISTS_EXCEPTION, cause);
    }
}
