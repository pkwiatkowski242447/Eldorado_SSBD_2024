package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.ParkingMessages;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingModifyDTO {

    @Schema(description = "Identifier of parking which is being edited.", example = "96a36faa-f2a2-41b8-9c3c-b6bef04ce6d1", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID parkingId;

    @Schema(description = "Address of the parking.")
    @NotNull(message = ParkingMessages.ADDRESS_NULL)
    private Address address;

    @Schema(description = "City in which the parking is located", example = "LA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "ZipCode of the city in which the parking is located", example = "11-111", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zipCode;

    @Schema(description = "Street of the city in which the parking is located", example = "white", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;

    public ParkingModifyDTO(String city, String zipCode, String street) {
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
    }

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
                .append("ParkingId: ", parkingId)
                .toString();
    }
}
