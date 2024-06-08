package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.ParkingBaseException;

/**
 * Used to specify an exception indicating some kind of conflict situation,
 * related to parking object.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking
 */
public class ParkingConflictException extends ParkingBaseException {

    public ParkingConflictException() {
    }

    public ParkingConflictException(String message) {
        super(message);
    }

    public ParkingConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParkingConflictException(Throwable cause) {
        super(cause);
    }
}
