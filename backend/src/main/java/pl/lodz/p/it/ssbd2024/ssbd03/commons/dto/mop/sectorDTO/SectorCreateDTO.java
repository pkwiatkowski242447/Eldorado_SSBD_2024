package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mop.SectorConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.SectorMessages;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class SectorCreateDTO {

    @Pattern(regexp = SectorConsts.SECTOR_NAME_PATTERN, message = SectorMessages.SECTOR_REGEX_NOT_MET)
    @Size(min = SectorConsts.SECTOR_NAME_LENGTH, max = SectorConsts.SECTOR_NAME_LENGTH, message = SectorMessages.SECTOR_NAME_INVALID)
    @Schema(description = "The name of the sector", example = "B9", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = SectorMessages.SECTOR_TYPE_NULL)
    @Enumerated(EnumType.STRING)
    @Schema(description = "The type of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Sector.SectorType type;

    @NotNull(message = SectorMessages.SECTOR_AVAILABLE_PLACES_NULL)
    @PositiveOrZero(message = SectorMessages.SECTOR_AVAILABLE_PLACES_NEGATIVE)
    @Schema(description = "The maximum number of parking spots in the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer maxPlaces;

    @NotNull(message = SectorMessages.SECTOR_WEIGHT_NULL)
    @Min(value = SectorConsts.SECTOR_WEIGHT_MIN_WEIGHT, message = SectorMessages.SECTOR_WEIGHT_TOO_SMALL)
    @Max(value = SectorConsts.SECTOR_WEIGHT_MAX_WEIGHT, message = SectorMessages.SECTOR_WEIGHT_TOO_LARGE)
    @Schema(description = "The weight of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer weight;

    @NotNull(message = SectorMessages.SECTOR_ACTIVE_NULL)
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
