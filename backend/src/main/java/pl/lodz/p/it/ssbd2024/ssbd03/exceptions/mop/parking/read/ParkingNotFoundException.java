package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.read;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an exception related with retrieving a parking from database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking
 */
public class ParkingNotFoundException extends ApplicationBaseException {

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
