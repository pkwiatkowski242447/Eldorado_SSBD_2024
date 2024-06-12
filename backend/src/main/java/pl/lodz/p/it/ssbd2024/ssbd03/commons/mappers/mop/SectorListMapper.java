package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

public class SectorListMapper {

    /**
     * This method is used to map Sector entity to an instance of SectorListDTO.
     * @param sector Sector to map.
     * @return Returns mapped SectorListDTO instance.
     */
    static public SectorListDTO toSectorListDTO(Sector sector) {
        return new SectorListDTO(sector.getId(),
                sector.getName(),
                sector.getType(),
                sector.getMaxPlaces(),
                sector.getOccupiedPlaces(),
                sector.getWeight(),
                sector.getDeactivationTime());
    }
}
