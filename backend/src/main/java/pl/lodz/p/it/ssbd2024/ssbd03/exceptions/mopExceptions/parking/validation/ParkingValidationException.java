package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions.parking.validation;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions.parking.ParkingBaseException;

public class ParkingValidationException extends ParkingBaseException {
    public ParkingValidationException() {}
    public ParkingValidationException(String message) {super(message);}
    public ParkingValidationException(String message, Throwable cause) {super(message, cause);}
    public ParkingValidationException(Throwable cause) {super(cause);}
}
