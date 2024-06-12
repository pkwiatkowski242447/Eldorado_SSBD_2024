package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class CannotEnterParkingException extends ParkingConflictException {

    public CannotEnterParkingException() {
        super(I18n.CANNOT_ENTER_PARKING_EXCEPTION);
    }

    public CannotEnterParkingException(String message) {
        super(message);
    }
}
