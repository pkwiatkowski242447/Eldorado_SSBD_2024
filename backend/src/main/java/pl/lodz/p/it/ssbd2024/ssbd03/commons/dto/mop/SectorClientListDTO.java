package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class SectorClientListDTO {

    @Schema(description = "Id of the sector", example = "73538016-095a-4564-965c-9a17c9ded334", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;
    @Schema(description = "The name of the sector", example = "B9", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "The type of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Sector.SectorType type;
    @Schema(description = "The maximum number of parking spots in the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer maxPlaces;
    @Schema(description = "The current number of available parking spots in the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer availablePlaces;

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
