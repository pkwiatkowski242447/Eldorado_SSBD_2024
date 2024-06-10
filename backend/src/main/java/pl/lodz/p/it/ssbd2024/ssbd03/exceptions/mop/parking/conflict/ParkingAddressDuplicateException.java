package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict;

import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

/**
 * Exception class related to the fact of repeating the same address - city, zip-code and street
 * for two different parking in the database.
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address
 * @see pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking
 */
public class ParkingAddressDuplicateException extends ParkingConflictException {

    public ParkingAddressDuplicateException() {
        super(I18n.PARKING_ADDRESS_DUPLICATE_EXCEPTION);
    }

    public ParkingAddressDuplicateException(Throwable cause) {
        super(I18n.PARKING_ADDRESS_DUPLICATE_EXCEPTION, cause);
    }
}
