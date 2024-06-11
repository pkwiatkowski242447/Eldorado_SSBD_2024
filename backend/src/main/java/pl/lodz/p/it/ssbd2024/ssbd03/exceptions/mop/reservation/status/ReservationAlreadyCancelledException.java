package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationAlreadyCancelledException extends ReservationStatusException {

    public ReservationAlreadyCancelledException() {
        super(I18n.RESERVATION_ALREADY_CANCELLED_EXCEPTION);
    }

    public ReservationAlreadyCancelledException(String message) {
        super(message);
    }
}
