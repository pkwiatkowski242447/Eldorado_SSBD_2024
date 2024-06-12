package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Reservation;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.time.LocalDateTime;

public class ReservationInvalidTimeframe extends ReservationBaseException {

    public ReservationInvalidTimeframe() {
        super(I18n.RESERVATION_INVALID_TIMEFRAME);
    }

    public ReservationInvalidTimeframe(String message) {
        super(message);
    }
}
