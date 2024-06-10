package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ParkingListMapper {
    static public ParkingOutputListDTO toParkingListDTO(Parking p) {
        List<String> sectorTypeList = new ArrayList<>();
        for (int i = 0; i < p.getSectors().size(); i++) {
            sectorTypeList.add(p.getSectors().get(i).getType().name());
        }
        return new ParkingOutputListDTO(p.getId(), p.getAddress().getCity(), p.getAddress().getZipCode(), p.getAddress().getStreet(),
                new ArrayList<>(new HashSet<>(sectorTypeList)), p.getSectorStrategy());
    }
}
