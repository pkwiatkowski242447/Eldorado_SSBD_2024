package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an exception indicating that the reservation
 * has already been expired.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation
 */
public class ReservationExpiredException extends ReservationStatusException {

    public ReservationExpiredException() {
        super(I18n.RESERVATION_EXPIRED_EXCEPTION);
    }

    public ReservationExpiredException(Throwable cause) {
        super(I18n.RESERVATION_EXPIRED_EXCEPTION, cause);
    }
}
