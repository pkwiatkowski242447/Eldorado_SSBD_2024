package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationSectorNonActiveException extends ReservationBaseException {

    public ReservationSectorNonActiveException() {
        super(I18n.RESERVATION_SECTOR_NON_ACTIVE);
    }

    public ReservationSectorNonActiveException(String message) {
        super(message);
    }
}
