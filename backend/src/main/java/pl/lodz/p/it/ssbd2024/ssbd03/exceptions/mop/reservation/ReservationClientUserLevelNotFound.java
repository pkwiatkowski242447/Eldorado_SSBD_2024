package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;


import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ReservationClientUserLevelNotFound extends ReservationBaseException {

    public ReservationClientUserLevelNotFound() {
        super(I18n.RESERVATION_CLIENT_USER_LEVEL_NOT_FOUND);
    }

    public ReservationClientUserLevelNotFound(String message) {
        super(message);
    }
}
