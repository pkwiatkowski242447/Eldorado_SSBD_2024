package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.allocationCodeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

/**
 * Data transfer object used for passing the allocation code, generated
 * at the beginning of the reservation to the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@LoggerInterceptor
public class AllocationCodeWithSectorDTO {

    @Schema(description = "Allocation code, generated when entering parking in a given reservation, used for ending it.", example = "76491929", requiredMode = Schema.RequiredMode.REQUIRED)
    private String allocationCode;
    @Schema(description = "Sector chosen for the given reservation", requiredMode = Schema.RequiredMode.REQUIRED)
    private Sector sector;
    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the MakeReservationDTO object.
     */

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}