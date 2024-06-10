package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class ParkingCreateDTO {

    @Schema(description = "The city where parking is located", example = "Warsaw", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "The zip code of city", example = "12-345", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zipCode;

    @Schema(description = "The street where parking is located", example = "Flower-street", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;
}
