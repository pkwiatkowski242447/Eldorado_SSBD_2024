package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.ParkingModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.ParkingOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;


/**
 * Used to handle Parking entity-DTO mapping.
 */
@LoggerInterceptor
public class ParkingMapper {

    /**
     * This method is used to map Parking entity to Parking output DTO class.
     * @param parking Parking to map.
     * @return Returns mapped Parking DTO class.
     */
    public static ParkingOutputDTO toParkingOutputDto(Parking parking) {

        return new ParkingOutputDTO(
                parking.getVersion(),
                parking.getId(),
                parking.getAddress().getCity(),
                parking.getAddress().getZipCode(),
                parking.getAddress().getStreet()
        );
    }

    /**
     * This method is used to map modified Parking DTO to Parking entity class.
     * @param parkingModifyDTO Parking DTO to map.
     * @return Returns mapped Parking entity class.
     */
    public static Parking toParking(ParkingModifyDTO parkingModifyDTO){
        Address address = new Address(parkingModifyDTO.getCity(),parkingModifyDTO.getZipCode(), parkingModifyDTO.getStreet());
        Parking parking = new Parking(address);

        return parking;
    }

}
