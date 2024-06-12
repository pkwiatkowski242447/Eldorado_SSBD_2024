package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationExceedingMaximumTime extends ReservationBaseException {

    public ReservationExceedingMaximumTime() {
        super(I18n.RESERVATION_EXCEEDING_MAXIMUM_TIME);
    }

    public ReservationExceedingMaximumTime(String message) {
        super(message);
    }
}
