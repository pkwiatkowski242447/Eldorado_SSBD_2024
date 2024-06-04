package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop;

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
public class SectorOutputDTO {

    @Schema(description = "The name of the sector", example = "BC-69", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
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