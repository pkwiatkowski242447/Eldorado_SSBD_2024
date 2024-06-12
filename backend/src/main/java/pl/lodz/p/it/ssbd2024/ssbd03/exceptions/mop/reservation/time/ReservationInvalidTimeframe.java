package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.time;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationInvalidTimeframe extends ReservationTimeException {

    public ReservationInvalidTimeframe() {
        super(I18n.RESERVATION_INVALID_TIMEFRAME);
    }

    public ReservationInvalidTimeframe(String message) {
        super(message);
    }
}
