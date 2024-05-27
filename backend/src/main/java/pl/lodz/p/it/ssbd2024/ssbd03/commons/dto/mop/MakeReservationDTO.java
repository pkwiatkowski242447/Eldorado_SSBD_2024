package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data transfer object used in making new reservation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MakeReservationDTO {

    @Schema(description = "Identifier of sector in which a place is being reserved.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID sectorId;
    @Schema(description = "Reservation start time.", example = "2024-05-25T13:40:54.922Z", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;
    @Schema(description = "Reservation end time.", example = "2024-05-25T15:40:54.922Z", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

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
                .append("SectorId: ", this.sectorId)
                .append("BeginTime: ", this.beginTime)
                .append("EndTime: ", this.endTime)
                .toString();
    }
}