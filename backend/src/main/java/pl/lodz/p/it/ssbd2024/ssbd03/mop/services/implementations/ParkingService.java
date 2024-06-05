package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.AllocationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.AllocationCodeWithSectorDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Address;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyActiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read.SectorNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.read.ParkingNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyInactiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ParkingServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.util.List;
import java.util.UUID;

/**
 * Service managing Parking and Sectors.
 *
 * @see Parking
 * @see Sector
 */
@Slf4j
@Service
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class ParkingService implements ParkingServiceInterface {

    private final ParkingFacade parkingFacade;

    @Autowired
    public ParkingService(ParkingFacade parkingFacade) {
        this.parkingFacade = parkingFacade;
    }

    @Override
    @RolesAllowed(Authorities.ADD_PARKING)
    public Parking createParking(String city, String zipCode, String street) throws ApplicationBaseException {
        Address address = new Address(city, zipCode, street);
        Parking parking = new Parking(address);
        log.error(parking.getSectors().toString());
        this.parkingFacade.create(parking);
        return parking;
    }

    @Override
    @RolesAllowed({Authorities.ADD_SECTOR, Authorities.GET_PARKING})
    public void createSector(UUID parkingId, String name, Sector.SectorType type, Integer maxPlaces, Integer weight, Boolean active) throws ApplicationBaseException {
        Parking parking = parkingFacade.findAndRefresh(parkingId).orElseThrow(ParkingNotFoundException::new);
        Sector sector = new Sector(parking, name, type, maxPlaces, weight, active);

        parkingFacade.createSector(sector);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_PARKING)
    public List<Parking> getAllParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        return parkingFacade.findAllParkingWithPagination(pageNumber, pageSize);
    }

    @Override
    @RolesAllowed(Authorities.GET_SECTOR)
    public Sector getSectorById(UUID id) throws ApplicationBaseException {
        return parkingFacade.findSectorById(id).orElseThrow(SectorNotFoundException::new);
    }

    @Override
    @RolesAllowed(Authorities.GET_PARKING)
    public Parking getParkingById(UUID id) throws ApplicationBaseException {
        return parkingFacade.findAndRefresh(id).orElseThrow(ParkingNotFoundException::new);
    }

    @Override
    @RolesAllowed(Authorities.ACTIVATE_SECTOR)
    public void activateSector(UUID id) throws ApplicationBaseException {
        Sector sector = parkingFacade.findAndRefreshSectorById(id).orElseThrow(SectorNotFoundException::new);
        if(sector.getActive()) throw new SectorAlreadyActiveException();
        sector.setActive(true);
        parkingFacade.editSector(sector);
    }

    @Override
    @RolesAllowed(Authorities.DEACTIVATE_SECTOR)
    public void deactivateSector(UUID id) throws ApplicationBaseException {
        Sector sector = parkingFacade.findAndRefreshSectorById(id).orElseThrow(SectorNotFoundException::new);
        if(!sector.getActive()) throw new SectorAlreadyInactiveException();
        sector.setActive(false);
        parkingFacade.editSector(sector);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_SECTORS)
    public List<Sector> getSectorsByParkingId(UUID id) throws ApplicationBaseException {
        return parkingFacade.findSectorsInParking(id);
    }

    @Override
    @RolesAllowed(Authorities.DELETE_PARKING)
    public void removeParkingById(UUID id) throws ApplicationBaseException {
        Parking parking = this.parkingFacade.findAndRefresh(id).orElseThrow(ParkingNotFoundException::new);
        this.parkingFacade.removeParkingById(parking.getId());
    }

    @Override
    @RolesAllowed(Authorities.ENTER_PARKING_WITH_RESERVATION)
    public AllocationCodeDTO enterParkingWithReservation(UUID reservationId, String userName) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.EDIT_PARKING)
    public Parking editParking(Parking modifiedParking, UUID id) throws ApplicationBaseException {
        Parking foundParking = parkingFacade.findAndRefresh(id).orElseThrow(()-> new ParkingNotFoundException(I18n.PARKING_NOT_FOUND_EXCEPTION));

        if (!modifiedParking.getVersion().equals(foundParking.getVersion())){
            throw new OptimisticLockException();
        }
        Address address = new Address(modifiedParking.getAddress().getCity(),modifiedParking.getAddress().getZipCode(),modifiedParking.getAddress().getStreet());
        foundParking.setAddress(address);
        parkingFacade.edit(foundParking);
        return foundParking;
    }

    @Override
    @RolesAllowed(Authorities.EDIT_SECTOR)
    public Sector editSector(Sector modifiedSector) throws ApplicationBaseException {
        Sector foundSector = parkingFacade.findSectorById(modifiedSector.getId()).orElseThrow(SectorNotFoundException::new);

        if (!modifiedSector.getVersion().equals(foundSector.getVersion())) {
            throw new ApplicationOptimisticLockException();
        }

        foundSector.setType(modifiedSector.getType());
        foundSector.setMaxPlaces(modifiedSector.getMaxPlaces());
        foundSector.setWeight(modifiedSector.getWeight());

        parkingFacade.editSector(foundSector);

        return foundSector;
    }

    @Override
    @RolesAllowed(Authorities.DELETE_SECTOR)
    public void removeSectorById(UUID id) throws ApplicationBaseException{
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.GET_ALL_AVAILABLE_PARKING)
    public List<Parking> getAvailableParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.ENTER_PARKING_WITHOUT_RESERVATION)
    public AllocationCodeWithSectorDTO enterParkingWithoutReservation(String userName) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    @RolesAllowed(Authorities.EXIT_PARKING)
    public void exitParking(UUID reservationId, String exitCode) throws ApplicationBaseException {
        throw new UnsupportedOperationException(I18n.UNSUPPORTED_OPERATION_EXCEPTION);
    }
}
