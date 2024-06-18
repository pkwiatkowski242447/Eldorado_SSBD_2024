package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingHistoryDataOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingHistoryData;

/**
 * Used to handle ParkingHistoryData entity-DTO mapping.
 */
@LoggerInterceptor
public class ParkingHistoryDataMapper {

    /**
     * This method is used to map ParkingHistoryData entity to ParkingHistoryData output DTO class.
     *
     * @param parking ParkingHistoryData to map.
     * @return Returns mapped ParkingHistoryData DTO class.
     */
    public static ParkingHistoryDataOutputDTO toParkingHistoryDataOutputDto(ParkingHistoryData parking) {

        return new ParkingHistoryDataOutputDTO(
                parking.getId(),
                parking.getVersion(),
                parking.getCity(),
                parking.getStreet(),
                parking.getZipCode(),
                parking.getStrategy(),
                parking.getModificationTime(),
                parking.getModifiedBy() == null ? "" : parking.getModifiedBy().getLogin()
        );
    }

}
