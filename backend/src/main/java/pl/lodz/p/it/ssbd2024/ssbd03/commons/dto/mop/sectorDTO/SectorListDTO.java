package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO;

import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class SectorListDTO {

    @Schema(description = "The identifier of the sector", example = "4ce920a0-6f4d-4e95-ba24-99ba32b66491", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "The name of the sector", example = "SA-02", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Determines whether the sector is active", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean active;

    @Schema(description = "The type of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Sector.SectorType type;

    @Schema(description = "The maximum number of parking spots in the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer maxPlaces;

    @Schema(description = "The current number of available parking spots in the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer availablePlaces;

    @Schema(description = "The weight of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer weight;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the SectorCreateDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .toString();
    }
}
