package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.integrity.UserLevelMissingException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.conflict.CannotExitParkingException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.parking.read.ParkingNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientAccountNonEnabledException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientLimitException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationClientUserLevelNotFound;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.ReservationNoAvailablePlaceException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.read.ReservationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status.ReservationExpiredException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.reservation.status.ReservationNotStartedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.SectorInvalidDeactivationTimeException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyActiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.edit.SectorEditOfTypeOrMaxPlacesWhenActiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.read.SectorNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mop.sector.status.SectorAlreadyInactiveException;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.AccountMOPFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ParkingFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.ReservationFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.facades.UserLevelMOPFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ParkingServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.SectorDeterminationStrategy.LeastOccupied;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.SectorDeterminationStrategy.LeastOccupiedWeighted;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.SectorDeterminationStrategy.MostOccupied;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.SectorDeterminationStrategy.SectorStrategy;

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
    private final UserLevelMOPFacade userLevelMOPFacade;

    @Value("${reservation.max_hours}")
    private Integer reservationMaxHours;

    @Value("${reservation.client_limit}")
    private Integer clientLimit;

    @Autowired
    public ParkingService(ParkingFacade parkingFacade,
                          ReservationFacade reservationFacade,
                          AccountMOPFacade accountFacade,
                          UserLevelMOPFacade userLevelMOPFacade) {
        this.parkingFacade = parkingFacade;
        this.reservationFacade = reservationFacade;
        this.accountFacade = accountFacade;
        this.userLevelMOPFacade = userLevelMOPFacade;
    }

    // MOP.2 - Add parking

    @Override
    @RolesAllowed({Authorities.ADD_PARKING})
    public Parking createParking(String city, String zipCode, String street, Parking.SectorDeterminationStrategy strategy) throws ApplicationBaseException {
        Address address = new Address(city, zipCode, street);
        Parking parking = new Parking(address, strategy);
        this.parkingFacade.create(parking);
        return parking;
    }

    // MOP.6 - Create sector

    @Override
    @RolesAllowed({Authorities.ADD_SECTOR})
    public void createSector(UUID parkingId, String name, Sector.SectorType type, Integer maxPlaces, Integer weight) throws ApplicationBaseException {
        Parking parking = parkingFacade.findAndRefresh(parkingId).orElseThrow(ParkingNotFoundException::new);
        Sector sector = new Sector(parking, name, type, maxPlaces, weight);

        parkingFacade.createSector(sector);
    }

    // MOP.1 - Get all parking

    @Override
    @RolesAllowed({Authorities.GET_ALL_PARKING})
    public List<Parking> getAllParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        return parkingFacade.findAllParkingWithPagination(pageNumber, pageSize);
    }

    // MOP.13 - Get sector

    @Override
    @RolesAllowed({Authorities.GET_SECTOR})
    public Sector getSectorById(UUID id) throws ApplicationBaseException {
        return parkingFacade.findAndRefreshSectorById(id).orElseThrow(SectorNotFoundException::new);
    }

    // MOP.12 - Get parking

    @Override
    @RolesAllowed({Authorities.GET_PARKING, Authorities.EDIT_PARKING})
    public Parking getParkingById(UUID id) throws ApplicationBaseException {
        return parkingFacade.findAndRefresh(id).orElseThrow(ParkingNotFoundException::new);
    }

    // MOP.9 - Activate sector

    @Override
    @RolesAllowed(Authorities.ACTIVATE_SECTOR)
    public void activateSector(UUID id) throws ApplicationBaseException {
        Sector sector = parkingFacade.findAndRefreshSectorById(id).orElseThrow(SectorNotFoundException::new);
        if (sector.getActive(this.reservationMaxHours)) throw new SectorAlreadyActiveException();
        sector.activateSector();
        parkingFacade.editSector(sector);
    }

    // MOP.10 - Deactivate sector

    @Override
    @RolesAllowed(Authorities.DEACTIVATE_SECTOR)
    public void deactivateSector(UUID id, LocalDateTime deactivationTime) throws ApplicationBaseException {
        Sector sector = parkingFacade.findAndRefreshSectorById(id).orElseThrow(SectorNotFoundException::new);

        if (!sector.getActive(this.reservationMaxHours)) throw new SectorAlreadyInactiveException();
        if (!deactivationTime.isAfter(LocalDateTime.now().plusHours(this.reservationMaxHours))) throw new SectorInvalidDeactivationTimeException();

        List<Reservation> reservations = this.reservationFacade.getAllReservationsToCancelBeforeDeactivation(sector.getId(),
                deactivationTime.minusHours(this.reservationMaxHours));

        for (Reservation reservation : reservations) {
            reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
            this.reservationFacade.edit(reservation);
        }

        sector.deactivateSector(LocalDateTime.now());
        parkingFacade.editSector(sector);
    }

    // MOP.5 - Get all sectors

    @Override
    @RolesAllowed({Authorities.GET_ALL_SECTORS, Authorities.GET_PARKING})
    public List<Sector> getSectorsByParkingId(UUID id, boolean active, int pageNumber, int pageSize) throws ApplicationBaseException {
        this.parkingFacade.findAndRefresh(id).orElseThrow(ParkingNotFoundException::new);
        return parkingFacade.findSectorsInParking(id, active, pageNumber, pageSize);
    }

    // MOP.3 - Remove parking

    @Override
    @RolesAllowed({Authorities.DELETE_PARKING})
    public void removeParkingById(UUID id) throws ApplicationBaseException {
        Parking parking = this.parkingFacade.findAndRefresh(id).orElseThrow(ParkingNotFoundException::new);
        this.parkingFacade.removeParkingById(parking.getId());
    }

    @Override
    @RolesAllowed({Authorities.ENTER_PARKING_WITH_RESERVATION})
    public void enterParkingWithReservation(UUID reservationId, String userName) throws ApplicationBaseException {
        Reservation reservation = this.reservationFacade.findAndRefresh(reservationId).orElseThrow(ReservationNotFoundException::new);
        Account account = this.accountFacade.findByLogin(userName).orElseThrow(AccountNotFoundException::new);

        if (reservation.getBeginTime().isAfter(LocalDateTime.now())) {
            throw new ReservationNotStartedException();
        } else if (reservation.getEndTime().isBefore(LocalDateTime.now())) {
            throw new ReservationExpiredException();
        }

        if (reservation.getSector().getOccupiedPlaces() == 0) {
            throw new ReservationNoAvailablePlaceException();
        }

        // Current user is not the owner of the reservation
        if (account.getUserLevels().stream().noneMatch(userLevel -> reservation.getClient().getId().equals(userLevel.getId()))) {
            throw new UserLevelMissingException(I18n.USER_NOT_RESERVATION_OWNER_EXCEPTION);
        }

        if (reservation.getStatus().equals(Reservation.ReservationStatus.AWAITING)) {
            reservation.getSector().setOccupiedPlaces(reservation.getSector().getOccupiedPlaces() - 1);
            reservation.setStatus(Reservation.ReservationStatus.IN_PROGRESS);
        }

        ParkingEvent entryEvent = new ParkingEvent(LocalDateTime.now(), ParkingEvent.EventType.ENTRY);
        reservation.addParkingEvent(entryEvent);
        this.reservationFacade.edit(reservation);
    }

    // MOP.4 - Edit parking

    @Override
    @RolesAllowed({Authorities.EDIT_PARKING})
    public Parking editParking(Parking modifiedParking, UUID parkingId) throws ApplicationBaseException {
        Parking foundParking = parkingFacade.findAndRefresh(parkingId).orElseThrow(ParkingNotFoundException::new);

        if (!modifiedParking.getVersion().equals(foundParking.getVersion())) {
            throw new ApplicationOptimisticLockException();
        }

        Address address = new Address(modifiedParking.getAddress().getCity(),
                modifiedParking.getAddress().getZipCode(),
                modifiedParking.getAddress().getStreet());

        foundParking.setAddress(address);
        parkingFacade.edit(foundParking);
        return foundParking;
    }

    // MOP.8 - Edit sector

    @Override
    @RolesAllowed({Authorities.EDIT_SECTOR})
    public Sector editSector(UUID id, Long version, Sector modifiedSector) throws ApplicationBaseException {
        Sector foundSector = parkingFacade.findAndRefreshSectorById(id).orElseThrow(SectorNotFoundException::new);

        if (!version.equals(foundSector.getVersion())) {
            throw new ApplicationOptimisticLockException();
        }

        if (!foundSector.getType().equals(modifiedSector.getType())) {
            if (foundSector.getActive(this.reservationMaxHours)) {
                throw new SectorEditOfTypeOrMaxPlacesWhenActiveException();
            }
            foundSector.setType(modifiedSector.getType());
        }

        if (!foundSector.getMaxPlaces().equals(modifiedSector.getMaxPlaces())) {
            if (foundSector.getActive(this.reservationMaxHours)) {
                throw new SectorEditOfTypeOrMaxPlacesWhenActiveException();
            }
            foundSector.setMaxPlaces(modifiedSector.getMaxPlaces());
        }

        foundSector.setWeight(modifiedSector.getWeight());

        parkingFacade.editSector(foundSector);

        return foundSector;
    }

    // MOP.7 - Remove sector

    @Override
    @RolesAllowed({Authorities.DELETE_SECTOR})
    public void removeSectorById(UUID id) throws ApplicationBaseException {
        Sector sector = this.parkingFacade.findAndRefreshSectorById(id).orElseThrow(SectorNotFoundException::new);
        this.parkingFacade.removeSector(sector);
    }

    // MOP.11 - Get all available parking

    @Override
    @RolesAllowed({Authorities.GET_ALL_AVAILABLE_PARKING})
    public List<Parking> getAvailableParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        return parkingFacade.findAllAvailableParkingWithPagination(pageNumber, pageSize);
    }

    @Override
    @RolesAllowed(Authorities.ENTER_PARKING_WITHOUT_RESERVATION)
    public Reservation enterParkingWithoutReservation(UUID parkingId, String login, boolean isAnonymous) throws ApplicationBaseException {
        Client client = null;
        Client.ClientType clientType = Client.ClientType.BASIC;
        LocalDateTime currentTime = LocalDateTime.now();

        //checks when entry is not anonymous
        if (!isAnonymous) {
            client = userLevelMOPFacade.findGivenUserLevelForGivenAccount(login).orElseThrow(ReservationClientUserLevelNotFound::new);

            // Check client availability
            if (!client.getAccount().isEnabled()) throw new ReservationClientAccountNonEnabledException();

            // Check client reservations limit
            if (reservationFacade.countAllActiveUserReservationByLogin(login) + 1 > clientLimit)
                throw new ReservationClientLimitException();

            clientType = client.getType();
        }
        List<Sector> result = parkingFacade.getAvailableSectorsNow(clientType, parkingId, currentTime, reservationMaxHours);

        //TODO add multiple algorithms for determining sector assignment
        if (result.isEmpty()) throw new ReservationNoAvailablePlaceException();
        SectorStrategy sectorStrategy = switch (result.getFirst().getParking().getSectorStrategy()) {
            case LEAST_OCCUPIED -> new LeastOccupied();
            case MOST_OCCUPIED -> new MostOccupied();
            case LEAST_OCCUPIED_WEIGHTED -> new LeastOccupiedWeighted();
        };
        Sector chosenSector = sectorStrategy.choose(result);

        Reservation reservation = new Reservation(client, chosenSector, currentTime);
        reservation.setStatus(Reservation.ReservationStatus.IN_PROGRESS);
        reservationFacade.create(reservation);

        chosenSector.setOccupiedPlaces(chosenSector.getOccupiedPlaces() + 1);
        parkingFacade.editSector(chosenSector);

        return reservation;
    }

    @Override
    @RolesAllowed(Authorities.EXIT_PARKING)
    public void exitParking(UUID reservationId, String userLogin, boolean endReservation) throws ApplicationBaseException {
        Reservation reservation = this.reservationFacade.findAndRefresh(reservationId).orElseThrow(ReservationNotFoundException::new);

        // Check if the user is the "owner" of the reservation (if the reservation has a client assigned to it)
        if (reservation.getClient() != null) {
            Account account = this.accountFacade.findByLogin(userLogin).orElseThrow(AccountNotFoundException::new);
            if (!account.getUserLevels().contains(reservation.getClient())) {
                throw new ReservationNotFoundException();
            }
        }

        // Check if the number of entry parking events is greater than the number of exit events
        long entryEventsCount = reservation.getParkingEvents().stream()
                .filter(event -> event.getType() == ParkingEvent.EventType.ENTRY)
                .count();

        long exitEventsCount = reservation.getParkingEvents().stream()
                .filter(event -> event.getType() == ParkingEvent.EventType.EXIT)
                .count();

        // If the number of entry events is less than or equal to the number of exit events,
        // the user cannot exit the parking
        if (entryEventsCount <= exitEventsCount) {
            throw new CannotExitParkingException();
        }

        // If reservation is made for anonymous user or the reservation needs to be finished
        if (reservation.getClient() == null || endReservation) {
            // Case for anonymous user - end time is being set
            if (reservation.getClient() == null) reservation.setEndTime(LocalDateTime.now());
            reservation.getSector().setOccupiedPlaces(reservation.getSector().getOccupiedPlaces() - 1);
            reservation.setStatus(Reservation.ReservationStatus.COMPLETED_MANUALLY);
        }

        ParkingEvent exitEvent = new ParkingEvent(LocalDateTime.now(), ParkingEvent.EventType.EXIT);
        reservation.addParkingEvent(exitEvent);

        this.reservationFacade.edit(reservation);
    }
}
