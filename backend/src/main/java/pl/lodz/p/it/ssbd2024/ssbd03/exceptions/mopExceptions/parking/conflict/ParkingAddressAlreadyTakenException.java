package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions.parking.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

public class ParkingAddressAlreadyTakenException extends ParkingConflictException {
    public ParkingAddressAlreadyTakenException() {super(I18n.PARKING_ADDRESS_ALREADY_TAKEN);}
}
