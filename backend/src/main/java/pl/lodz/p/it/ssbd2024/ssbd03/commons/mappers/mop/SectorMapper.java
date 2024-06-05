package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

/*
 * Used to handle Sector entity-DTO mapping.
 */
public class SectorMapper {

    /**
     * This method is used to map Sector entity to an instance of SectorOutputDTO.
     * @param sector Sector to map.
     * @return Returns mapped SectorOutputDTO instance.
     */
    public static SectorOutputDTO toSectorOutputDTO(Sector sector) {
        return new SectorOutputDTO(
            sector.getName(),
            sector.getParking().getId(),
            sector.getType(),
            sector.getMaxPlaces(),
            sector.getWeight(),
            sector.getActive()
        );
    }
}
