package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop.AddressConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.AddressMessages;

@Getter
@Setter
@NoArgsConstructor
@LoggerInterceptor
public class ParkingModifyDTO extends ParkingSignableDTO {

    @Schema(description = "City in which the parking is located", example = "LA", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = AddressMessages.CITY_BLANK)
    @Pattern(regexp = AddressConsts.CITY_REGEX, message = AddressMessages.CITY_REGEX_NOT_MET)
    @Size(min = AddressConsts.CITY_MIN_LENGTH, message = AddressMessages.CITY_NAME_TOO_SHORT)
    @Size(max = AddressConsts.CITY_MAX_LENGTH, message = AddressMessages.CITY_NAME_TOO_LONG)
    private String city;

    @Schema(description = "ZipCode of the city in which the parking is located", example = "11-111", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = AddressMessages.ZIP_CODE_BLANK)
    @Pattern(regexp = AddressConsts.ZIP_CODE_REGEX, message = AddressMessages.ZIP_CODE_REGEX_NOT_MET)
    @Size(min = AddressConsts.ZIP_CODE_LENGTH, message = AddressMessages.ZIP_CODE_INVALID)
    @Size(max = AddressConsts.ZIP_CODE_LENGTH, message = AddressMessages.ZIP_CODE_INVALID)
    private String zipCode;

    @Schema(description = "Street of the city in which the parking is located", example = "white", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = AddressMessages.STREET_BLANK)
    @Pattern(regexp = AddressConsts.STREET_REGEX, message = AddressMessages.STREET_REGEX_NOT_MET)
    @Size(min = AddressConsts.STREET_MIN_LENGTH, message = AddressMessages.STREET_NAME_TOO_SHORT)
    @Size(max = AddressConsts.STREET_MAX_LENGTH, message = AddressMessages.STREET_NAME_TOO_LONG)
    private String street;

    public ParkingModifyDTO(String city, String zipCode, String street) {
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the ParkingModifyDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("city: ", city)
                .append("zipCode: ", zipCode)
                .append("street: ", street)
                .toString();
    }
}
