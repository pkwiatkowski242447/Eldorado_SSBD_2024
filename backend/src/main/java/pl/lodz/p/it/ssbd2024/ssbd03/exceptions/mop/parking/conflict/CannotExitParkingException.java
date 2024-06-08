package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.ParkingBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to indicate that the user cannot exit the parking.
 */
public class CannotExitParkingException extends ParkingBaseException {

    public CannotExitParkingException() {
        super(I18n.CANNOT_EXIT_PARKING_EXCEPTION);
    }

    public CannotExitParkingException(Throwable cause) {
        super(I18n.CANNOT_EXIT_PARKING_EXCEPTION, cause);
    }
}
