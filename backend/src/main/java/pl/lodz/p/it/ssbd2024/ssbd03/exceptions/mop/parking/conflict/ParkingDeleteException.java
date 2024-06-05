package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related with deleting a parking from the database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking
 */
public class ParkingDeleteException extends ParkingConflictException {

    public ParkingDeleteException() {
        super(I18n.PARKING_DELETE_EXCEPTION);
    }

    public ParkingDeleteException(Throwable cause) {
        super(I18n.PARKING_DELETE_EXCEPTION, cause);
    }
}
