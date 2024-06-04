package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an Exception related with retrieving an Parking from database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking
 */
public class ParkingNotFoundException extends ParkingBaseException {

    public ParkingNotFoundException() {
        super(I18n.PARKING_NOT_FOUND_EXCEPTION);
    }

    public ParkingNotFoundException(String message) {
        super(message);
    }

    public ParkingNotFoundException(Throwable cause) {
        super(I18n.PARKING_NOT_FOUND_EXCEPTION, cause);
    }

    public ParkingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
