package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

public class SectorListMapper {

    /**
     * This method is used to map Sector entity to an instance of SectorListDTO.
     * @param s Sector to map.
     * @return Returns mapped SectorListDTO instance.
     */
    static public SectorListDTO toSectorListDTO(Sector s) {
        return new SectorListDTO(s.getName(), s.getActive(), s.getType(),
            s.getMaxPlaces(), s.getOccupiedPlaces(), s.getWeight());
    }
}
