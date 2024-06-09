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
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.DTOMessages;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class ParkingCreateDTO {
    @Schema(description = "The City where parking is located", example = "Warsaw", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = DTOMessages.CITY_BLANK)
    @Pattern(regexp = DTOConsts.CITY_REGEX, message = DTOMessages.CITY_REGEX_NOT_MET)
    @Size(min = DTOConsts.CITY_MIN_LENGTH, message = DTOMessages.CITY_TOO_SHORT)
    @Size(max = DTOConsts.CITY_MAX_LENGTH, message = DTOMessages.CITY_TOO_LONG)
    private String city;

    @Schema(description = "The Zip code of city", example = "12-345", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = DTOMessages.ZIP_CODE_BLANK)
    @Pattern(regexp = DTOConsts.ZIP_CODE_REGEX, message = DTOMessages.ZIP_CODE_REGEX_NOT_MET)
    @Size(min = DTOConsts.ZIP_CODE_MIN_LENGTH, message = DTOMessages.ZIP_CODE_TOO_SHORT)
    @Size(max = DTOConsts.ZIP_CODE_MAX_LENGTH, message = DTOMessages.ZIP_CODE_TOO_LONG)
    private String zipCode;

    @Schema(description = "The Street where parking is located", example = "Al. Flower-street", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = DTOMessages.STREET_BLANK)
    @Pattern(regexp = DTOConsts.STREET_REGEX, message = DTOMessages.STREET_REGEX_NOT_MET)
    @Size(min = DTOConsts.STREET_MIN_LENGTH, message = DTOMessages.STREET_TOO_SHORT)
    @Size(max = DTOConsts.STREET_MAX_LENGTH, message = DTOMessages.STREET_TOO_LONG)
    private String street;
}
