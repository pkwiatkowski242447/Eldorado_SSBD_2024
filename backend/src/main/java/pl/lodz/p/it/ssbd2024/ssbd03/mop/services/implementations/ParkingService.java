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
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.allocationCodeDTO.AllocationCodeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mop.allocationCodeDTO.AllocationCodeWithSectorDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.integrity.UserLevelMissingException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationNoAvailablePlaceException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.read.ReservationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status.ReservationExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status.ReservationNotStartedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyActiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read.SectorNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.read.ParkingNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.AccountMOPFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.EntryCodeFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyInactiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ReservationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ParkingServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.time.LocalDateTime;
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
    private final ReservationFacade reservationFacade;
    private final AccountMOPFacade accountFacade;
    private final EntryCodeFacade entryCodeFacade;

    @Autowired
    public ParkingService(ParkingFacade parkingFacade,
                          ReservationFacade reservationFacade,
                          AccountMOPFacade accountFacade,
                          EntryCodeFacade entryCodeFacade) {
        this.parkingFacade = parkingFacade;
        this.reservationFacade = reservationFacade;
        this.accountFacade = accountFacade;
        this.entryCodeFacade = entryCodeFacade;
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
        return parkingFacade.findAndRefreshSectorById(id).orElseThrow(SectorNotFoundException::new);
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
    @RolesAllowed({Authorities.GET_ALL_SECTORS, Authorities.GET_PARKING})
    public List<Sector> getSectorsByParkingId(UUID id, boolean active, int pageNumber, int pageSize) throws ApplicationBaseException {
        return parkingFacade.findSectorsInParking(id, active, pageNumber, pageSize);
    }

    @Override
    @RolesAllowed(Authorities.DELETE_PARKING)
    public void removeParkingById(UUID id) throws ApplicationBaseException {
        Parking parking = this.parkingFacade.findAndRefresh(id).orElseThrow(ParkingNotFoundException::new);
        this.parkingFacade.removeParkingById(parking.getId());
    }

    @Override
    @RolesAllowed({Authorities.ENTER_PARKING_WITH_RESERVATION})
    public AllocationCodeDTO enterParkingWithReservation(UUID reservationId, String userName) throws ApplicationBaseException {
        Reservation reservation = this.reservationFacade.findAndRefresh(reservationId).orElseThrow(ReservationNotFoundException::new);
        Account account = this.accountFacade.findByLogin(userName).orElseThrow(AccountNotFoundException::new);

        if (reservation.getBeginTime().isAfter(LocalDateTime.now())) {
            throw new ReservationNotStartedException();
        } else if (reservation.getEndTime().isBefore(LocalDateTime.now())) {
            throw new ReservationExpiredException();
        }

        if (reservation.getSector().getAvailablePlaces() == 0) {
            throw new ReservationNoAvailablePlaceException();
        }

        // Current user is not the owner of the reservation
        if (account.getUserLevels().stream().noneMatch(userLevel -> reservation.getClient().getId().equals(userLevel.getId()))) {
            throw new UserLevelMissingException(I18n.USER_NOT_RESERVATION_OWNER_EXCEPTION);
        }

        EntryCode entryCode;
        if (reservation.getParkingEvents().stream().anyMatch(parkingEvent -> parkingEvent.getType().equals(ParkingEvent.EventType.ENTRY))) {
            // Entry code was already generated for that reservation
            entryCode = this.entryCodeFacade.findEntryCodeByReservationId(reservationId).orElseThrow();
        } else {
            // Entry code generation
            int entryCodeValue = (int) ((Math.random() * 90000000) + 1000000);
            entryCode = new EntryCode(String.valueOf(entryCodeValue), reservation);
            this.entryCodeFacade.create(entryCode);

            reservation.getSector().setAvailablePlaces(reservation.getSector().getAvailablePlaces() - 1);
        }

        ParkingEvent entryEvent = new ParkingEvent(LocalDateTime.now(), ParkingEvent.EventType.ENTRY);
        reservation.addParkingEvent(entryEvent);
        this.reservationFacade.edit(reservation);
        return new AllocationCodeDTO(entryCode.getEntryCode());
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
        Sector foundSector = parkingFacade.findAndRefreshSectorById(modifiedSector.getId()).orElseThrow(SectorNotFoundException::new);

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
