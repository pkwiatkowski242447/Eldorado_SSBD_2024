package pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mop;

import org.springframework.beans.factory.annotation.Autowired;

import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.SectorOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mapper.MapperBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;

public class SectorMapper {

    @Autowired
    private static ParkingFacade parkingFacade;

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

    public static Sector toSector(SectorModifyDTO sectorModifyDTO)
            throws MapperBaseException, ApplicationBaseException {
        // TODO: Throw some non-default exception
        Parking parking = parkingFacade.find(sectorModifyDTO.getParkingId()).orElseThrow();

        return new Sector(
            parking,
            sectorModifyDTO.getName(),
            sectorModifyDTO.getType(),
            sectorModifyDTO.getMaxPlaces(),
            sectorModifyDTO.getWeight(),
            sectorModifyDTO.getActive()
        );
    }
}
