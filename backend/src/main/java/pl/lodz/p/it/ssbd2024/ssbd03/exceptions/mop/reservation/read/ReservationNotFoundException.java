package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.read;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationNotFoundException extends ReservationBaseException {

    public ReservationNotFoundException() {
        super(I18n.RESERVATION_NOT_FOUND_EXCEPTION);
    }

    public ReservationNotFoundException(Throwable cause) {
        super(I18n.RESERVATION_NOT_FOUND_EXCEPTION, cause);
    }
}
