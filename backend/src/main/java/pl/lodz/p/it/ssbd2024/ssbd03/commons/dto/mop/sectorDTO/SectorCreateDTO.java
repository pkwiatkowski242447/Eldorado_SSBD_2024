package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.annotations.ValueOfEnum;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class SectorCreateDTO {

    @Schema(description = "The name of the sector", example = "B9", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "The type of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValueOfEnum(enumClass = Sector.SectorType.class, message = "piwo")
    private String type;
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
     * @return String representation of the SectorCreateDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
