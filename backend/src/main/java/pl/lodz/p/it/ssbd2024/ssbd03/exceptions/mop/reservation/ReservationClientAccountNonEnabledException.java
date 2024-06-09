package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationClientAccountNonEnabledException extends ReservationBaseException {
    public ReservationClientAccountNonEnabledException() {
        super(I18n.RESERVATION_CLIENT_ACCOUNT_NON_ENABLED);
    }

    public ReservationClientAccountNonEnabledException(String message) {
        super(message);
    }
}
