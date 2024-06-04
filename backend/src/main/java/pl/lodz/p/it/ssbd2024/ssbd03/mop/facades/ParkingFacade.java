package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

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
     * Persists a new Parking to the database.
     *
     * @param entity Parking to be persisted.
     */
    @Override
    @RolesAllowed(Authorities.ADD_PARKING)
    public void create(Parking entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Forces modification of the Parking in the database.
     *
     * @param entity Parking to be modified.
     */
    @Override
    @RolesAllowed({Authorities.EDIT_PARKING, Authorities.ADD_PARKING,
            Authorities.DELETE_SECTOR, Authorities.EDIT_SECTOR,
            Authorities.ACTIVATE_SECTOR, Authorities.DEACTIVATE_SECTOR}
    )
    public void edit(Parking entity) throws ApplicationBaseException {
        super.edit(entity);
    }

    /**
     * Retrieves a Parking by the ID and forces its refresh.
     *
     * @param id ID of the Parking to be retrieved.
     * @return If a Parking with the given ID was found returns an Optional containing the Parking, otherwise returns an empty Optional.
     */
    @Override
    @RolesAllowed({Authorities.GET_PARKING, Authorities.DELETE_PARKING, Authorities.EDIT_PARKING, Authorities.ADD_SECTOR,
            Authorities.DELETE_SECTOR, Authorities.EDIT_SECTOR, Authorities.RESERVE_PARKING_PLACE,
            Authorities.CANCEL_RESERVATION, Authorities.ENTER_PARKING_WITHOUT_RESERVATION,
            Authorities.ENTER_PARKING_WITH_RESERVATION, Authorities.EXIT_PARKING}
    )
    public Optional<Parking> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves a Parking by the ID.
     *
     * @param id ID of the Parking to be retrieved.
     * @return If a Parking with the given ID was found returns an Optional containing the Parking, otherwise returns an empty Optional.
     */
    @Override
    @DenyAll
    public Optional<Parking> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Retrieves all Parking.
     *
     * @return List containing all Parking.
     */
    @Override
    @DenyAll
    public List<Parking> findAll() throws ApplicationBaseException {
        return super.findAll();
    }

    @RolesAllowed(Authorities.GET_ALL_PARKING)
    public List<Parking> findAllWithPagination(int page, int pageSize, boolean showOnlyActive) throws ApplicationBaseException {
        var list = getEntityManager().createNamedQuery("Parking.findAll", Parking.class)
                .setFirstResult(page * pageSize)
                .setParameter("showOnlyActive", showOnlyActive)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    @DenyAll
    public List<Parking> findParkingBySectorTypes(
            List<Sector.SectorType> sectorTypes, int page, int pageSize, boolean showOnlyActive) throws ApplicationBaseException {
        var list = getEntityManager().
                createNamedQuery("Parking.findBySectorTypes", Parking.class)
                .setParameter("sectorTypes", sectorTypes)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    @RolesAllowed({Authorities.RESERVE_PARKING_PLACE, Authorities.GET_ALL_AVAILABLE_PARKING})
    public List<Parking> findParkingWithAvailablePlaces(int page, int pageSize, boolean showOnlyActive) throws ApplicationBaseException {
        var list = getEntityManager().
                createNamedQuery("Parking.findWithAvailablePlaces", Parking.class)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Removes a Parking from the database.
     *
     * @param entity Parking to be removed from the database.
     */
    @Override
    @RolesAllowed(Authorities.DELETE_PARKING)
    public void remove(Parking entity) throws ApplicationBaseException {
        super.remove(entity);
    }

    /**
     * Counts the number of the Parking in the database.
     *
     * @return Number of Parking in the database.
     */
    @Override
    @DenyAll
    public int count() throws ApplicationBaseException {
        return super.count();
    }


    // -- SECTORS --

    /**
     * Retrieves a Sector by the ID.
     *
     * @param id ID of the Sector to be retrieved.
     * @return If a Sector with the given ID was found returns an Optional containing the Sector, otherwise returns an empty Optional.
     */
    @DenyAll
    protected Optional<Sector> findSectorById(UUID id) throws ApplicationBaseException {
        return Optional.ofNullable(getEntityManager().find(Sector.class, id));
    }

    /**
     * Retrieves a Sector by the ID and forces its refresh.
     *
     * @param id ID of the Sector to be retrieved.
     * @return If a Sector with the given ID was found returns an Optional containing the Sector, otherwise returns an empty Optional.
     */
    @RolesAllowed({Authorities.GET_SECTOR, Authorities.DELETE_SECTOR, Authorities.EDIT_SECTOR,
            Authorities.CANCEL_RESERVATION, Authorities.ENTER_PARKING_WITHOUT_RESERVATION,
            Authorities.ENTER_PARKING_WITH_RESERVATION, Authorities.EXIT_PARKING, Authorities.ACTIVATE_SECTOR})
    public Optional<Sector> findAndRefreshSectorById(UUID id) throws ApplicationBaseException {
        Optional<Sector> optEntity = findSectorById(id);
        optEntity.ifPresent(t -> getEntityManager().refresh(t));
        return optEntity;
    }

    @RolesAllowed({Authorities.GET_SECTOR, Authorities.DELETE_SECTOR, Authorities.EDIT_SECTOR})
    public Sector findSectorByParkingIdAndName(UUID parkingId, String name)
            throws ApplicationBaseException {
        var sector = getEntityManager().createNamedQuery("Sector.findByParkingIdAndName", Sector.class)
                .setParameter("parkingId", parkingId)
                .setParameter("name", name)
                .getSingleResult();
        return sector;
    }

    @RolesAllowed(Authorities.GET_ALL_SECTORS)
    public List<Sector> findSectorsInParking(UUID parkingId, boolean showOnlyActive)
            throws ApplicationBaseException {
        var list = getEntityManager().createNamedQuery("Sector.findAllInParking", Sector.class)
                .setParameter("parkingId", parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    @RolesAllowed(Authorities.GET_ALL_SECTORS)
    public List<Sector> findSectorsInParkingWithPagination(UUID parkingId, int page, int pageSize, boolean showOnlyActive)
            throws ApplicationBaseException {
        var list = getEntityManager().createNamedQuery("Sector.findAllInParking", Sector.class)
                .setParameter("parkingId", parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    @RolesAllowed(Authorities.RESERVE_PARKING_PLACE)
    public List<Sector> findSectorInParkingWithAvailablePlaces(UUID parkingId, int page, int pageSize, boolean showOnlyActive)
            throws ApplicationBaseException {
        var list = getEntityManager().createNamedQuery("Sector.findWithAvailablePlaces", Sector.class)
                .setParameter("parkingId", parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    @DenyAll
    public List<Sector> findSectorInParkingBySectorTypes(
            UUID parkingId, List<Sector.SectorType> sectorTypes, int page, int pageSize, boolean showOnlyActive) throws ApplicationBaseException {
        var list = getEntityManager().createNamedQuery("Sector.findBySectorTypes", Sector.class)
                .setParameter("parkingId", parkingId)
                .setParameter("sectorTypes", sectorTypes)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    /**
     * Persists a new Sector to the database.
     */
    @RolesAllowed(Authorities.ADD_SECTOR)
    public void createSector(Sector sector) throws ApplicationBaseException {
        getEntityManager().persist(sector);
        getEntityManager().flush();
    }

    /**
     * Removes a sector from the database.
     * This method will not single-handedly remove the sector from database.
     * To remove it you need to additionally update the parking and save changes to database.
     *
     * @param sector Sector to be removed from the database.
     */
    @RolesAllowed(Authorities.DELETE_SECTOR)
    public void removeSector(Sector sector) throws ApplicationBaseException {
        getEntityManager().remove(sector);
        getEntityManager().flush();
    }

    /**
     * Forces modification of the sector in the database.
     *
     * @param sector Sector to be modified.
     */
    @RolesAllowed({Authorities.EDIT_SECTOR, Authorities.ACTIVATE_SECTOR})
    public void editSector(Sector sector) throws ApplicationBaseException {
        getEntityManager().merge(sector);
        getEntityManager().flush();
    }

    /**
     * Forces a refresh on all elements in the list.
     *
     * @param list List of the Sectors to be refreshed.
     */
    @RolesAllowed({Authorities.GET_ALL_SECTORS, Authorities.RESERVE_PARKING_PLACE})
    protected void refreshAllSectors(List<Sector> list) throws ApplicationBaseException {
        if (list != null && !list.isEmpty()) {
            list.forEach(getEntityManager()::refresh);
        }
    }
}
