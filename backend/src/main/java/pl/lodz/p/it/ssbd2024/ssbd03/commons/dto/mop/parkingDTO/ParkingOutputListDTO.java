package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;

import java.util.List;
import java.util.UUID;

/**
 * Data transfer object used in returning parking .
 */
@Getter
@Setter
@AllArgsConstructor
public class ParkingOutputListDTO {

    @Schema(description = "UUID identifier linked with parking", example = "96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "City where parking is located", example = "BoatCity", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "Zip code related to City", example = "00-000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zipCode;

    @Schema(description = "Street where parking is located", example = "Palki", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;

    @Schema(description = "Sector types on parking", example = "[\"UNDERGROUND\", \"COVERED\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> sectorTypes;

    @Schema(description = "Strategy used in determining sector for entries without reservation", example="LEAST_OCCUPIED", requiredMode = Schema.RequiredMode.REQUIRED)
    private Parking.SectorDeterminationStrategy strategy;
}
