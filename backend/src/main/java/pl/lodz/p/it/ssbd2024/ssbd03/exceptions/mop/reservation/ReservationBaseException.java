package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

/**
 * General superclass for all exceptions related to the reservation object.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation
 */
public class ReservationBaseException extends ApplicationBaseException {

    public ReservationBaseException() {
    }

    public ReservationBaseException(String message) {
        super(message);
    }

    public ReservationBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationBaseException(Throwable cause) {
        super(cause);
    }
}
