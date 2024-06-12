package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationInsufficientClientType extends ReservationBaseException {

    public ReservationInsufficientClientType() {
        super(I18n.RESERVATION_INSUFFICIENT_CLIENT_TYPE);
    }

    public ReservationInsufficientClientType(String message) {
        super(message);
    }
}
