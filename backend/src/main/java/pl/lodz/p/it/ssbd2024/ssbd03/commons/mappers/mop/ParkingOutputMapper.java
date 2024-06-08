package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorClientListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.parkingDTO.ParkingClientOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.List;
import java.util.stream.Collectors;

public class ParkingOutputMapper {
    static public ParkingClientOutputDTO toParkingOutputDTO(Parking p) {
        List<SectorClientListDTO> sectorClientList = p.getSectors()
                .stream()
                .filter(Sector::getActive)
                .map(SectorClientListMapper::toSectorClientListDTO)
                .collect(Collectors.toList());
        return new ParkingClientOutputDTO(p.getId(), p.getAddress().getCity(), p.getAddress().getZipCode(), p.getAddress().getStreet(), sectorClientList);
    }
}
