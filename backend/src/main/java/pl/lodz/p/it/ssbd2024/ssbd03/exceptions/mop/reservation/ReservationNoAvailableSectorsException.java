package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Used to specify an exception indicating that there are no available sectors in the parking for the given client level
 */
public class ReservationNoAvailableSectorsException extends ReservationBaseException {

    public ReservationNoAvailableSectorsException() {
        super(I18n.RESERVATION_SECTOR_NO_AVAILABLE_SECTORS_EXCEPTION);
    }

    public ReservationNoAvailableSectorsException(Throwable cause) {
        super(I18n.RESERVATION_SECTOR_NO_AVAILABLE_SECTORS_EXCEPTION, cause);
    }
}
