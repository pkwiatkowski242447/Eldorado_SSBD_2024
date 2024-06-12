package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationAlreadyEndedException extends ReservationStatusException {
    public ReservationAlreadyEndedException() {
        super(I18n.RESERVATION_ALREADY_ENDED_EXCEPTION);
    }

    public ReservationAlreadyEndedException(String message) {
        super(message);
    }
}
