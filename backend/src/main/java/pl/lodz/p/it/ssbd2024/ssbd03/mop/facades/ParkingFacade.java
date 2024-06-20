package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage Parking Entities.
 */
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class ParkingFacade extends AbstractFacade<Parking> {

    @Value("${reservation.max_hours}")
    private int reservationMaxLength;

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public ParkingFacade() {
        super(Parking.class);
    }

    /**
     * Retrieves an entity manager.
     *
     * @return Entity manager associated with the facade.
     */
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Persists a new parking entity object to the database.
     *
     * @param entity Parking to be persisted.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({Authorities.ADD_PARKING})
    public void create(Parking entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Forces modification of the parking entity object in the database.
     *
     * @param entity Parking to be modified.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({
            Authorities.EDIT_PARKING, Authorities.ADD_PARKING,
            Authorities.DELETE_SECTOR, Authorities.EDIT_SECTOR,
            Authorities.ACTIVATE_SECTOR, Authorities.DEACTIVATE_SECTOR
    })
    public void edit(Parking entity) throws ApplicationBaseException {
        super.edit(entity);
    }

    /**
     * Retrieves a parking entity object by the ID and forces its refresh.
     *
     * @param id Identifier of the parking to be retrieved from the database.
     * @return If a Parking with the given ID was found returns an Optional containing the Parking, otherwise returns an empty Optional.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({
            Authorities.GET_PARKING, Authorities.DELETE_PARKING, Authorities.EDIT_PARKING, Authorities.ADD_SECTOR,
            Authorities.DELETE_SECTOR, Authorities.EDIT_SECTOR, Authorities.RESERVE_PARKING_PLACE,
            Authorities.CANCEL_RESERVATION, Authorities.ENTER_PARKING_WITHOUT_RESERVATION,
            Authorities.ENTER_PARKING_WITH_RESERVATION, Authorities.EXIT_PARKING
    })
    public Optional<Parking> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * This method is used to retrieve all parking entity objects from the database, taking into account
     * pagination settings.
     *
     * @param pageNumber     Number of the page with parking entries to be retrieved from the database.
     * @param pageSize       Number of the parking entries per page.
     * @param showOnlyActive Boolean flag indicating whether this query should return only parking with active sectors.
     * @return List of parking entities matching certain criteria, if a form of list - or empty list - if no matching
     * parking entities were found.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_ALL_PARKING})
    public List<Parking> findAllWithPagination(int pageNumber, int pageSize, boolean showOnlyActive) throws ApplicationBaseException {
        var list = getEntityManager().createNamedQuery("Parking.findAll", Parking.class)
                .setFirstResult(pageNumber * pageSize)
                .setParameter("showOnlyActive", showOnlyActive)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * This method is used to retrieve parking with active sectors that have available places for
     * new reservations left.
     *
     * @param pageNumber     Number of the page with parking entities to be returned.
     * @param pageSize       Number of the parking entries per page.
     * @param showOnlyActive Boolean flag indicating whether this query should return only parking with active sectors.
     * @return List of parking entities if a form of list - or empty list - if no matching parking were found.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.RESERVE_PARKING_PLACE, Authorities.GET_ALL_AVAILABLE_PARKING})
    public List<Parking> findParkingWithAvailablePlaces(int pageNumber, int pageSize, boolean showOnlyActive) throws ApplicationBaseException {
        var list = getEntityManager().
                createNamedQuery("Parking.findWithAvailablePlaces", Parking.class)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Removes a parking entity object from the database.
     *
     * @param entity Parking object to be removed from the database.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @Override
    @RolesAllowed({Authorities.DELETE_PARKING})
    public void remove(Parking entity) throws ApplicationBaseException {
        super.remove(entity);
    }

    /**
     * Removes parking with given identifier (in form of UUID)
     * from the database.
     *
     * @param parkingId Identifier (UUID) of the parking to be removed.
     * @throws ApplicationBaseException General superclass of all the exceptions
     *                                  that could be thrown by aspects intercepting exceptions in the facade layer.
     */
    @RolesAllowed({Authorities.DELETE_PARKING})
    public void removeParkingById(UUID parkingId) throws ApplicationBaseException {
        getEntityManager().createNamedQuery("Parking.removeParkingById")
                .setParameter("parkingId", parkingId)
                .executeUpdate();
    }

    // -- SECTORS --

    /**
     * Retrieves a Sector by the ID.
     *
     * @param id ID of the Sector to be retrieved.
     * @return If a Sector with the given ID was found returns an Optional containing the Sector, otherwise returns an empty Optional.
     * @throws ApplicationBaseException General superclass of all the exceptions
     *                                  that could be thrown by aspects intercepting exceptions in the facade layer.
     */
    @RolesAllowed({Authorities.GET_SECTOR})
    private Optional<Sector> findSectorById(UUID id) throws ApplicationBaseException {
        return Optional.ofNullable(getEntityManager().find(Sector.class, id));
    }

    /**
     * Retrieves a Sector by the ID and forces its refresh.
     *
     * @param id ID of the Sector to be retrieved.
     * @return If a Sector with the given ID was found returns an Optional containing the Sector, otherwise returns an empty Optional.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({
            Authorities.GET_SECTOR, Authorities.DELETE_SECTOR, Authorities.EDIT_SECTOR,
            Authorities.CANCEL_RESERVATION, Authorities.ENTER_PARKING_WITHOUT_RESERVATION,
            Authorities.ENTER_PARKING_WITH_RESERVATION, Authorities.EXIT_PARKING, Authorities.ACTIVATE_SECTOR
    })
    public Optional<Sector> findAndRefreshSectorById(UUID id) throws ApplicationBaseException {
        Optional<Sector> optEntity = findSectorById(id);
        optEntity.ifPresent(t -> getEntityManager().refresh(t));
        return optEntity;
    }

    /**
     * This method is used to retrieve all sectors in the parking object in the database.
     *
     * @param parkingId  Identifier of the parking entity, which sectors are to be retrieved.
     * @param active     Boolean flag indicating status of the retrieved sectors - if true then only active sectors are returned.
     * @param pageNumber Number of the page with sector entries.
     * @param pageSize   Size of the page with sector entries for given parking.
     * @return List of the sectors from given page of given size, retrieved for certain parking.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_ALL_SECTORS, Authorities.GET_PARKING})
    public List<Sector> findSectorsInParking(UUID parkingId, boolean active, int pageNumber, int pageSize)
            throws ApplicationBaseException {
        var list = getEntityManager().createNamedQuery("Sector.findAllInParking", Sector.class)
                .setParameter("parkingId", parkingId)
                .setParameter("showOnlyActive", active)
                .setParameter("deactivationMinimum", LocalDateTime.now().plusDays(this.reservationMaxLength))
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    /**
     * This method is used to retrieve all sectors in a given parking, identified by its identifier with
     * only active / or both active and inactive sectors.
     *
     * @param parkingId      Identifier of the parking, which sectors are to be retrieved.
     * @param pageNumber     Number of the page with sectors entries to be returned.
     * @param pageSize       Number of the sectors entry per page.
     * @param showOnlyActive Boolean flag indicating whether only active or both active and inactive sectors should be returned.
     * @return List of the sectors from given parking, identified by its id, or empty list if given parking
     * does not have any sectors.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_ALL_SECTORS})
    public List<Sector> findSectorsInParkingWithPagination(UUID parkingId, int pageNumber, int pageSize, boolean showOnlyActive)
            throws ApplicationBaseException {
        var list = getEntityManager().createNamedQuery("Sector.findAllInParking", Sector.class)
                .setParameter("parkingId", parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setParameter("deactivationMinimum", LocalDateTime.now().plusHours(this.reservationMaxLength))
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    /**
     * This method is used to find active sectors with available places, used when making new reservation.
     *
     * @param parkingId      Identifier of the parking, which sectors are to be retrieved.
     * @param pageNumber     Number of the page with sectors entries to be returned.
     * @param pageSize       Number of the sectors entry per page.
     * @param showOnlyActive Boolean flag indicating whether only active or both active and inactive sectors should be returned.
     * @return List of the sectors from given parking, identified by its id, or empty list if given parking
     * does not have any sectors.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.RESERVE_PARKING_PLACE})
    public List<Sector> findSectorInParkingWithAvailablePlaces(UUID parkingId, int pageNumber, int pageSize, boolean showOnlyActive)
            throws ApplicationBaseException {
        var list = getEntityManager().createNamedQuery("Sector.findWithAvailablePlaces", Sector.class)
                .setParameter("parkingId", parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    /**
     * This method is used to persist sector entity object to the database.
     *
     * @param sector Sector to be persisted into the database.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.ADD_SECTOR})
    public void createSector(Sector sector) throws ApplicationBaseException {
        getEntityManager().persist(sector);
        getEntityManager().flush();
    }

    /**
     * Removes a sector from the database.
     * This method will not remove the sector from database completely.
     * To remove it you need to additionally update the parking and save changes to database.
     *
     * @param sector Sector to be removed from the database.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.DELETE_SECTOR})
    public void removeSector(Sector sector) throws ApplicationBaseException {
        getEntityManager().remove(sector);
        getEntityManager().flush();
    }

    /**
     * Forces modification of the sector in the database.
     *
     * @param sector Sector to be modified.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({
            Authorities.EDIT_SECTOR, Authorities.DEACTIVATE_SECTOR,
            Authorities.ACTIVATE_SECTOR, Authorities.END_RESERVATION,
            Authorities.ENTER_PARKING_WITHOUT_RESERVATION
    })
    public void editSector(Sector sector) throws ApplicationBaseException {
        getEntityManager().merge(sector);
        getEntityManager().flush();
    }

    /**
     * Forces a refresh on all elements in the list.
     *
     * @param list List of the Sectors to be refreshed.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_ALL_SECTORS, Authorities.RESERVE_PARKING_PLACE})
    protected void refreshAllSectors(List<Sector> list) throws ApplicationBaseException {
        if (list != null && !list.isEmpty()) {
            list.forEach(getEntityManager()::refresh);
        }
    }

    /***
     * Get all parking from the database.
     *
     * @param pageNumber Number of the page with parking to be retrieved.
     * @param pageSize Number of parking per page.
     * @return List of all parking from a specified page, of a given page size.
     * If a persistence exception is thrown, then empty list is returned.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_ALL_PARKING})
    public List<Parking> findAllParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<Parking> findAllParking = entityManager.createNamedQuery("Parking.findAllParking", Parking.class);
            findAllParking.setFirstResult(pageNumber * pageSize);
            findAllParking.setMaxResults(pageSize);
            List<Parking> list = findAllParking.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is to return all available parking, that is parking where there is at least one
     * sector with available places.
     *
     * @param pageNumber Number of the page with parking entries, to be returned.
     * @param pageSize   Number of parking entries per page.
     * @return List of parking, available to the end user, which means that new reservations or entries from
     * the street can occur. Otherwise, empty list is returned.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed({Authorities.GET_ALL_AVAILABLE_PARKING})
    public List<Parking> findAllAvailableParkingWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<Parking> findAllAvailableParking = entityManager.createNamedQuery("Parking.findAllAvailableParking", Parking.class);
            findAllAvailableParking.setFirstResult(pageNumber * pageSize);
            findAllAvailableParking.setMaxResults(pageSize);
            findAllAvailableParking.setParameter("deactivationMinimum", LocalDateTime.now().plusHours(this.reservationMaxLength));
            List<Parking> list = findAllAvailableParking.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * This method is used to force incrementing version field on the parking entity object
     * for given sector. Used when updating the aggregate.
     *
     * @param sector Sector which causes version increment in the parking entity object.
     * @throws ApplicationBaseException General superclass of all the exceptions thrown by the
     *                                  facade exception handling aspect.
     */
    @RolesAllowed(Authorities.RESERVE_PARKING_PLACE)
    public void forceVersionUpdate(Sector sector) throws ApplicationBaseException {
        getEntityManager().lock(sector, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }


    /**
     * Retrieves all sectors available for entry for given client type in a given parking.
     * @param clientType Client type determining type of sectors included.
     * @param parkingId ID of the parking searched.
     * @param now Time of entry to the parking. It needs to be passed to keep it the same as start of the reservation.
     * @param maxReservationHours Maximum time that a vehicle can spend on the parking.
     * @return List of sector available for entry.
     * @throws ApplicationBaseException when other problem occurred.
     */
    @RolesAllowed({Authorities.ENTER_PARKING_WITHOUT_RESERVATION})
    public List<Sector> getAvailableSectorsNow(Client.ClientType clientType, UUID parkingId, LocalDateTime now, int maxReservationHours) throws ApplicationBaseException {
        TypedQuery<Sector> query = entityManager.createNamedQuery("Reservation.getAvailableBasicSectorsNow", Sector.class)
                .setParameter("deactivationMinimum", LocalDateTime.now().plusHours(this.reservationMaxLength));
       if( clientType == Client.ClientType.STANDARD){
            query = entityManager.createNamedQuery("Reservation.getAvailableStandardSectorsNow", Sector.class)
                    .setParameter("deactivationMinimum", LocalDateTime.now().plusHours(this.reservationMaxLength));
        } else if( clientType == Client.ClientType.PREMIUM){
            query = entityManager.createNamedQuery("Reservation.getAvailablePremiumSectorsNow", Sector.class)
                    .setParameter("deactivationMinimum", LocalDateTime.now().plusHours(this.reservationMaxLength));
        }
        query.setParameter("parkingId", parkingId);
        query.setParameter("currentTime", now);
        query.setParameter("currentTimePlusReserve", now.plusHours(maxReservationHours));
        query.setParameter("currentTimeMinusReserve", now.minusHours(maxReservationHours));
        return query.getResultList();
    }
}
