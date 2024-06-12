package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@LoggerInterceptor
public class SectorOutputDTO extends SectorSignableDTO {

    @Schema(description = "The type of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Sector.SectorType type;

    @Schema(description = "The maximum number of parking spots in the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer maxPlaces;

    @Schema(description = "The weight of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer weight;

    @Schema(description = "Date and time marking deactivation time of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime deactivationTime;

    public SectorOutputDTO(UUID id,
                           UUID parkingId,
                           Long version,
                           String name,
                           Sector.SectorType type,
                           Integer maxPlaces,
                           Integer weight,
                           LocalDateTime deactivationTime) {
        super(id, parkingId, version, name);
        this.type = type;
        this.maxPlaces = maxPlaces;
        this.weight = weight;
        this.deactivationTime = deactivationTime;
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
