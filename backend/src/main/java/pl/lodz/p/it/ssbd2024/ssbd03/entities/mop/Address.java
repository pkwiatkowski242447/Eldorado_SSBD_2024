package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop.AddressConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.AddressMessages;

import java.io.Serial;
import java.io.Serializable;

/**
 * Stores information about address of the Parking.
 *
 * @see Parking
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The city where the parking is located.
     */
    @Pattern(regexp = AddressConsts.CITY_REGEX, message = AddressMessages.CITY_REGEX_NOT_MET)
    @Size(min = AddressConsts.CITY_MIN_LENGTH, message = AddressMessages.CITY_NAME_TOO_SHORT)
    @Size(max = AddressConsts.CITY_MAX_LENGTH, message = AddressMessages.CITY_NAME_TOO_LONG)
    @Column(name = DatabaseConsts.PARKING_CITY_COLUMN, nullable = false)
    private String city;

    /**
     * The ZIP code of the parking location.
     */
    @Pattern(regexp = AddressConsts.ZIP_CODE_REGEX, message = AddressMessages.ZIP_CODE_REGEX_NOT_MET)
    @Size(min = AddressConsts.ZIP_CODE_LENGTH, max = AddressConsts.ZIP_CODE_LENGTH, message = AddressMessages.ZIP_CODE_INVALID)
    @Column(name = DatabaseConsts.PARKING_ZIP_CODE_COLUMN, nullable = false, length = 6)
    private String zipCode;

    /**
     * The street where the parking is located.
     */
    @Pattern(regexp = AddressConsts.STREET_REGEX, message = AddressMessages.STREET_REGEX_NOT_MET)
    @Size(min = AddressConsts.STREET_MIN_LENGTH, message = AddressMessages.STREET_NAME_TOO_SHORT)
    @Size(max = AddressConsts.STREET_MAX_LENGTH, message = AddressMessages.STREET_NAME_TOO_LONG)
    @Column(name = DatabaseConsts.PARKING_STREET_COLUMN, nullable = false)
    private String street;
}
