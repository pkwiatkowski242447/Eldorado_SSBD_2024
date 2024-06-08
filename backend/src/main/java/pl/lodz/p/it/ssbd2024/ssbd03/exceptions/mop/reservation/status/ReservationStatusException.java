package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationBaseException;

/**
 * Used to specify an exception indicating invalid state of the reservation
 * object.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation
 */
public class ReservationStatusException extends ReservationBaseException {

    public ReservationStatusException() {
    }

    public ReservationStatusException(String message) {
        super(message);
    }

    public ReservationStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationStatusException(Throwable cause) {
        super(cause);
    }
}
