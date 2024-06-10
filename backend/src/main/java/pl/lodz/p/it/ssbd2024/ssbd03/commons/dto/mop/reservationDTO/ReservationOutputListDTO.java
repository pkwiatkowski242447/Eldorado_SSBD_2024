package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.reservationDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReservationOutputListDTO {
    @Schema(description = "UUID identifier linked with parking", example = "96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;
    @Schema(description = "Parking address - city", example = "BoatCity", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;
    @Schema(description = "Parking address - zip code", example = "00-000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zipCode;
    @Schema(description = "Parking address - street", example = "Palki", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;
    @Schema(description = "Sector name", example = "SA-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sectorName;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "Reservation begin time", example = "YYYY-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime beginTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "Reservation ending time", example = "YYYY-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endingTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "UUID identifier of the client linked with the reservation", example = "c276cb93-5cfe-4bf5-9998-ecdeee8ba06b")
    private UUID clientId;
}
