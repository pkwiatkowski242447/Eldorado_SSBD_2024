package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.SignableDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.Map;
import java.util.UUID;

/**
 * Data transfer object used as a basis for Sector singed DTOs.
 *
 * @see Sector
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@LoggerInterceptor
public abstract class SectorSignableDTO implements SignableDTO {

    @Schema(description = "The identifier of the sector", example = "4ce920a0-6f4d-4e95-ba24-99ba32b66491", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;
    @Schema(description = "The identifier of the parking containing the sector", example = "96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID parkingId;
    @Schema(description = "Number of object version", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long version;
    @Schema(description = "The name of the sector", example = "BC-69", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @JsonIgnore
    @Override
    public Map<String, ?> getSigningFields() {
        return Map.ofEntries(
                Map.entry("id", id.toString()),
                Map.entry("parking", parkingId.toString()),
                Map.entry("name", name),
                Map.entry("version", version)
        );
    }
}
