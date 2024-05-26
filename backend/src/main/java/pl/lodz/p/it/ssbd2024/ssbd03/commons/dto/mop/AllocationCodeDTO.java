package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data transfer object used for passing the allocation code, generated
 * at the beginning of the reservation to the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllocationCodeDTO {

    @Schema(description = "Allocation code, generated when entering parking in a given reservation, used for ending it.", example = "76491929", requiredMode = Schema.RequiredMode.REQUIRED)
    private String allocationCode;

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
