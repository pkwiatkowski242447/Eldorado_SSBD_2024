package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;

import java.util.UUID;

/**
 * Data transfer object used in returning parking.
 */
@Getter
@Setter
@NoArgsConstructor
@LoggerInterceptor
public class ParkingOutputDTO extends ParkingSignableDTO {


    @Schema(description = "City in which the parking is located", example = "LA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "ZipCode of the city in which the parking is located", example = "11-111", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zipCode;

    @Schema(description = "Street of the city in which the parking is located", example = "white", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;
    @Schema(description = "Strategy used in determining sector for entries without reservation", example="LEAST_OCCUPIED", requiredMode = Schema.RequiredMode.REQUIRED)
    private Parking.SectorDeterminationStrategy strategy;

    /**
     * All arguments constructor for ParkingOutputDTO - with calling constructor of superclass.
     */
    public ParkingOutputDTO (Long version, UUID parkingId, String city, String zipCode, String street, Parking.SectorDeterminationStrategy strategy) {
        super(version, parkingId);
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.strategy = strategy;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the ParkingOutputDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("city: ", city)
                .append("zipCode: ", zipCode)
                .append("street: ", street)
                .append(super.toString())
                .toString();
    }
}
