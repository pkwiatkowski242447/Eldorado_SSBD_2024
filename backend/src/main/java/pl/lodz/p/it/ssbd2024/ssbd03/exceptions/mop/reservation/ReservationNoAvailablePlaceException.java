package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an exception indicating that the sector, which the place reservation
 * is for, has no available places.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation
 */
public class ReservationNoAvailablePlaceException extends ReservationBaseException {

    public ReservationNoAvailablePlaceException() {
        super(I18n.RESERVATION_SECTOR_NO_AVAILABLE_PLACES_EXCEPTION);
    }

    public ReservationNoAvailablePlaceException(Throwable cause) {
        super(I18n.RESERVATION_SECTOR_NO_AVAILABLE_PLACES_EXCEPTION, cause);
    }
}
