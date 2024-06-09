package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DTOConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop.AddressConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.DTOMessages;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.AddressMessages;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class ParkingCreateDTO {
    @Schema(description = "The City where parking is located", example = "Warsaw", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = AddressMessages.CITY_BLANK)
    @Pattern(regexp = AddressConsts.CITY_REGEX, message = AddressMessages.CITY_REGEX_NOT_MET)
    @Size(min = AddressConsts.CITY_MIN_LENGTH, message = AddressMessages.CITY_NAME_TOO_SHORT)
    @Size(max = AddressConsts.CITY_MAX_LENGTH, message = AddressMessages.CITY_NAME_TOO_LONG)
    private String city;

    @Schema(description = "The Zip code of city", example = "12-345", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = AddressMessages.ZIP_CODE_BLANK)
    @Pattern(regexp = AddressConsts.ZIP_CODE_REGEX, message = AddressMessages.ZIP_CODE_REGEX_NOT_MET)
    @Size(min = AddressConsts.ZIP_CODE_LENGTH, max = AddressConsts.ZIP_CODE_LENGTH, message = AddressMessages.ZIP_CODE_INVALID)
    private String zipCode;

    @Schema(description = "The Street where parking is located", example = "Al. Flower-street", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = AddressMessages.STREET_BLANK)
    @Pattern(regexp = AddressConsts.STREET_REGEX, message = AddressMessages.STREET_REGEX_NOT_MET)
    @Size(min = AddressConsts.STREET_MIN_LENGTH, message = AddressMessages.STREET_NAME_TOO_SHORT)
    @Size(max = AddressConsts.STREET_MAX_LENGTH, message = AddressMessages.STREET_NAME_TOO_LONG)
    private String street;
}
