package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.HashSet;
import java.util.Set;

public class ParkingListMapper {

    static public ParkingOutputListDTO toParkingListDTO(Parking parking) {
        Set<String> sectorTypeList = new HashSet<>();

        for (Sector sector : parking.getSectors()) {
            sectorTypeList.add(sector.getType().name());
        }

        return new ParkingOutputListDTO(
                parking.getId(),
                parking.getAddress().getCity(),
                parking.getAddress().getZipCode(),
                parking.getAddress().getStreet(),
                sectorTypeList.stream().toList()
        );
    }
}
