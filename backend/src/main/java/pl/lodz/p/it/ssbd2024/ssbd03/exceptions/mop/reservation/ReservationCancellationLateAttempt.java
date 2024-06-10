package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationCancellationLateAttempt extends ReservationBaseException {

    public ReservationCancellationLateAttempt() {
        super(I18n.RESERVATION_CANCELLATION_LATE_ATTEMPT);
    }

    public ReservationCancellationLateAttempt(String message) {
        super(message);
    }
}
