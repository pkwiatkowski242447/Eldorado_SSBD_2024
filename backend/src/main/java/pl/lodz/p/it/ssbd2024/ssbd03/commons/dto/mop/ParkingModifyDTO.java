package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

public class ParkingModifyDTO {

    @Schema(description = "Identifier of parking which is being edited.", example = "96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID parkingId;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the ParkingModifyDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
