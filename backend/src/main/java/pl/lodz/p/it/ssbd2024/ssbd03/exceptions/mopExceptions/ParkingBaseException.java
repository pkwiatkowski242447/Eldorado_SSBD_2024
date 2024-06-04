package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mopExceptions;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

public class ParkingBaseException extends ApplicationBaseException {

    public ParkingBaseException() {
    }

    public ParkingBaseException(String message) {
        super(message);
    }

    public ParkingBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParkingBaseException(Throwable cause) {
        super(cause);
    }
}
