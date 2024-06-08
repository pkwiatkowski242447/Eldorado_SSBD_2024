package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an exception indicating that the reservation
 * has not yet begun.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation
 */
public class ReservationNotStartedException extends ReservationStatusException {

    public ReservationNotStartedException() {
        super(I18n.RESERVATION_NOT_STARTED_EXCEPTION);
    }

    public ReservationNotStartedException(Throwable cause) {
        super(I18n.RESERVATION_NOT_STARTED_EXCEPTION, cause);
    }
}
