package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions.parking.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions.parking.ParkingBaseException;

public class ParkingConflictException extends ParkingBaseException {
    public ParkingConflictException() {}
    public ParkingConflictException(String message) {super(message);}
    public ParkingConflictException(String message, Throwable cause) {super(message, cause);}
    public ParkingConflictException(Throwable cause) {super(cause);}
}
