package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO;

import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

@Getter
@Setter
@NoArgsConstructor
@LoggerInterceptor
public class SectorOutputDTO extends SectorSignableDTO {

    @Schema(description = "The identifier of the parking containing this sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID parkingId;
    @Schema(description = "The type of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Sector.SectorType type;
    @Schema(description = "The maximum number of parking spots in the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer maxPlaces;
    @Schema(description = "The weight of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer weight;
    @Schema(description = "Determines whether the sector is active", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean active;

    public SectorOutputDTO(UUID id,
                           Long version,
                           String name,
                           UUID parkingId,
                           Sector.SectorType type,
                           Integer maxPlaces,
                           Integer weight,
                           Boolean active) {
        super(id, version, name);
        this.parkingId = parkingId;
        this.type = type;
        this.maxPlaces = maxPlaces;
        this.weight = weight;
        this.active = active;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the SectorOutputDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .toString();
    }
}
