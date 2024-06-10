package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationClientLimitException extends ReservationBaseException {
    public ReservationClientLimitException() {
        super(I18n.RESERVATION_CLIENT_LIMIT_EXCEPTION);
    }

    public ReservationClientLimitException(String message) {
        super(message);
    }
}
