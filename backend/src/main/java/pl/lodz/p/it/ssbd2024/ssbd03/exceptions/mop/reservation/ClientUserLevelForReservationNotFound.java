package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;


import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ClientUserLevelForReservationNotFound extends ReservationBaseException {

    public ClientUserLevelForReservationNotFound() {
        super(I18n.CLIENT_USER_LEVEL_FOR_RESERVATION_NOT_FOUND);
    }

    public ClientUserLevelForReservationNotFound(String message) {
        super(message);
    }
}
