package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop.SectorConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.SectorMessages;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@LoggerInterceptor
public class SectorModifyDTO extends SectorSignableDTO {

    @NotNull(message = SectorMessages.SECTOR_TYPE_NULL)
    @Enumerated(EnumType.STRING)
    @Schema(description = "The type of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Sector.SectorType type;

    @NotNull(message = SectorMessages.SECTOR_AVAILABLE_PLACES_NULL)
    @PositiveOrZero(message = SectorMessages.SECTOR_AVAILABLE_PLACES_NEGATIVE)
    @Max(value = SectorConsts.SECTOR_MAX_PLACES_MAX_VALUE, message = SectorMessages.SECTOR_MAX_PLACES_FULL)
    @Schema(description = "The maximum number of parking spots in the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer maxPlaces;

    @NotNull(message = SectorMessages.SECTOR_WEIGHT_NULL)
    @Min(value = SectorConsts.SECTOR_WEIGHT_MIN_WEIGHT, message = SectorMessages.SECTOR_WEIGHT_TOO_SMALL)
    @Max(value = SectorConsts.SECTOR_WEIGHT_MAX_WEIGHT, message = SectorMessages.SECTOR_WEIGHT_TOO_LARGE)
    @Schema(description = "The weight of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer weight;

    public SectorModifyDTO(UUID id,
                           Long version,
                           String name,
                           Sector.SectorType type,
                           Integer maxPlaces,
                           Integer weight) {
        super(id, version, name);
        this.type = type;
        this.maxPlaces = maxPlaces;
        this.weight = weight;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the SectorModifyDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .toString();
    }
}
