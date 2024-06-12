package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.time;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationBaseException;

public class ReservationTimeException extends ReservationBaseException {

    public ReservationTimeException() {
    }

    public ReservationTimeException(String message) {
        super(message);
    }

    public ReservationTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationTimeException(Throwable cause) {
        super(cause);
    }
}
