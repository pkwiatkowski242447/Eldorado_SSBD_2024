package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorClientListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

public class SectorClientListMapper {
    static public SectorClientListDTO toSectorClientListDTO(Sector s) {
        return new SectorClientListDTO(s.getId(), s.getName(), s.getType(), s.getMaxPlaces(), s.getAvailablePlaces());
    }
}
