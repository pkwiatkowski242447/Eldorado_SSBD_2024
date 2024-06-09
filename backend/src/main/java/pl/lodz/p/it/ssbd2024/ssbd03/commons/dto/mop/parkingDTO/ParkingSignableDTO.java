package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.SignableDTO;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@LoggerInterceptor
public abstract class ParkingSignableDTO implements SignableDTO {

    @NotNull
    @Schema(description = "Number of object version", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long version;

    @Schema(description = "Identifier of parking which is being edited.", example = "96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID parkingId;

    /**
     * This method returns Parking DTO properties  that should be signed.
     * @return Returns map of properties that should be signed.
     */
    @Override
    public Map<String, ?> getSigningFields() {
        return Map.ofEntries(
                Map.entry("version", version),
                Map.entry("parkingId",parkingId.toString())
        );
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the ParkingSignableDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Version: ", version)
                .append("parkingId: ", parkingId)
                .toString();
    }

}
