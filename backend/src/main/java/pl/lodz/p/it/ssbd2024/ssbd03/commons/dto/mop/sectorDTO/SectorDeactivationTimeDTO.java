package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectorDeactivationTimeDTO {

    @Schema(description = "Time of the deactivation of the sector", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deactivationTime;
}
