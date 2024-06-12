package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectorDeactivationTimeDTO {

    @Schema(description = "Time of the deactivation of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deactivationTime;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the SectorDeactivationTimeDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("deactivationTime", deactivationTime)
                .toString();
    }
}
