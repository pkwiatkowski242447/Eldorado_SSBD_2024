package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorClientListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

public class SectorClientListMapper {

    static public SectorClientListDTO toSectorClientListDTO(Sector sector) {
        return new SectorClientListDTO(sector.getId(),
                sector.getName(),
                sector.getType(),
                sector.getMaxPlaces(),
                sector.getOccupiedPlaces(),
                sector.getDeactivationTime());
    }
}
