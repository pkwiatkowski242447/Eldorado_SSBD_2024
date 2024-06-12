package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.sectorDTO.SectorOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

/**
 * Used to handle Sector entity-DTO mapping.
 */
public class SectorMapper {

    /**
     * This method is used to map Sector entity to an instance of SectorOutputDTO.
     *
     * @param sector Sector to map.
     * @return Returns mapped SectorOutputDTO instance.
     */
    public static SectorOutputDTO toSectorOutputDTO(Sector sector) {
        return new SectorOutputDTO(
                sector.getId(),
                sector.getParking().getId(),
                sector.getVersion(),
                sector.getName(),
                sector.getType(),
                sector.getMaxPlaces(),
                sector.getWeight(),
                sector.getDeactivationTime()
        );
    }

    public static Sector toSector(SectorModifyDTO sectorModifyDTO, Parking parking) {
        return new Sector(
            parking,
            sectorModifyDTO.getName(),
            sectorModifyDTO.getType(),
            sectorModifyDTO.getMaxPlaces(),
            sectorModifyDTO.getWeight()
        );
    }
}
