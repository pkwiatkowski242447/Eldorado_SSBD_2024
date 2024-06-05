package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

public class SectorListMapper {
    static public SectorListDTO toSectorListDTO(Sector s) {
        return new SectorListDTO(s.getName(), s.getActive(), s.getType(),
            s.getMaxPlaces(), s.getAvailablePlaces(), s.getWeight());
    }
}
