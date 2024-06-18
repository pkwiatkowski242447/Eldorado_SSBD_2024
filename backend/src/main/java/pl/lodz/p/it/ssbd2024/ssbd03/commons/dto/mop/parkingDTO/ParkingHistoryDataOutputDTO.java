package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data transfer object used in returning parking history data.
 */
@Getter
@Setter
@NoArgsConstructor
@LoggerInterceptor
public class ParkingHistoryDataOutputDTO {

    @Schema(description = "UUID identifier linked with parking", example = "73538016-095a-4564-965c-9a17c9ded334", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "Version of the parking data", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long version;

    @Schema(description = "City in which the parking is located", example = "Warszawa", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "Street on which the parking is located", example = "Zielona", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;

    @Schema(description = "Zip code of the parking", example = "51-145", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zipCode;

    @Schema(description = "Name of the algorithm determining to which sector the car will " +
            "be assigned if the entry is made without a reservation", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private Parking.SectorDeterminationStrategy strategy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "Data and time of modification of the account", example = "YYYY-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime modificationTime;

    @Schema(description = "Login of the account that made the change, may be null in case the change can't be tracked to a user", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String modifiedBy;

    /***
     * All arguments constructor for ParkingHistoryDataOutputDTO - with calling constructor of superclass.
     */
    public ParkingHistoryDataOutputDTO(UUID id, Long version,
                                       String city, String street,
                                       String zipCode, Parking.SectorDeterminationStrategy strategy,
                                       LocalDateTime modificationTime, String modifiedBy) {
        this.id = id;
        this.version = version;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.strategy = strategy;
        this.modificationTime = modificationTime;
        this.modifiedBy = modifiedBy;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AccountHistoryDataOutputDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Id: ", id).append(super.toString()).toString();
    }
}
