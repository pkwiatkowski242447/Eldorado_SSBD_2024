package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorClientListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingOutputListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingOutputMapper {
    static public ParkingOutputDTO toParkingOutputDTO(Parking p) {
        List<SectorClientListDTO> sectorClientList = p.getSectors()
                .stream()
                .filter(Sector::getActive)
                .map(SectorClientListMapper::toSectorClientListDTO)
                .collect(Collectors.toList());
        return new ParkingOutputDTO(p.getId(), p.getAddress().getCity(), p.getAddress().getZipCode(), p.getAddress().getStreet(), sectorClientList);
    }
}
