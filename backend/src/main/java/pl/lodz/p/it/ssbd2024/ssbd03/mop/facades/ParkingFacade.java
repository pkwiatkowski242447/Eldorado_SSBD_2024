package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
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
    @Transactional
    public void create(Parking entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Forces modification of the Parking in the database.
     *
     * @param entity Parking to be modified.
     */
    @Override
    @Transactional
    public void edit(Parking entity) throws ApplicationBaseException{
        super.edit(entity);
    }

    /**
     * Retrieves a Parking by the ID and forces its refresh.
     *
     * @param id ID of the Parking to be retrieved.
     * @return If a Parking with the given ID was found returns an Optional containing the Parking, otherwise returns an empty Optional.
     */
    @Override
    @Transactional
    public Optional<Parking> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    /**
     * Retrieves a Parking by the ID.
     *
     * @param id ID of the Parking to be retrieved.
     * @return If a Parking with the given ID was found returns an Optional containing the Parking, otherwise returns an empty Optional.
     */
    @Override
    @Transactional
    public Optional<Parking> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Retrieves all Parking.
     *
     * @return List containing all Parking.
     */
    @Override
    @Transactional
    public List<Parking> findAll() {
        return super.findAll();
    }

    @Transactional
    public List<Parking> findAllWithPagination(int page, int pageSize, boolean showOnlyActive){
        var list = getEntityManager().createNamedQuery("Parking.findAll", Parking.class)
                .setFirstResult(page * pageSize)
                .setParameter("showOnlyActive", showOnlyActive)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAll(list);
        return list;
    }

    @Transactional
    public List<Parking> findParkingsBySectorTypes(List<Sector.SectorType> sectorTypes, int page, int pageSize, boolean showOnlyActive){
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

    @Transactional
    public List<Parking> findParkingWithAvailablePlaces( int page, int pageSize, boolean showOnlyActive){
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
    @Transactional
    public void remove(Parking entity) {
        super.remove(entity);
    }

    /**
     * Counts the number of the Parking in the database.
     *
     * @return Number of Parking in the database.
     */
    @Override
    @Transactional
    public int count() {
        return super.count();
    }


    // -- SECTORS --

    /**
     * Retrieves a Sector by the ID.
     *
     * @param id ID of the Sector to be retrieved.
     * @return If a Sector with the given ID was found returns an Optional containing the Sector, otherwise returns an empty Optional.
     */
    @Transactional
    protected Optional<Sector> findSectorById(UUID id){
        return Optional.ofNullable(getEntityManager().find(Sector.class, id));
    }

    /**
     * Retrieves a Sector by the ID and forces its refresh.
     *
     * @param id ID of the Sector to be retrieved.
     * @return If a Sector with the given ID was found returns an Optional containing the Sector, otherwise returns an empty Optional.
     */
    @Transactional
    protected Optional<Sector> findAndRefreshSectorById(UUID id) {
        Optional<Sector> optEntity = findSectorById(id);
        optEntity.ifPresent(t -> getEntityManager().refresh(t));
        return optEntity;
    }

    @Transactional
    public List<Sector> findSectorsInParkingWithPagination(UUID parkingId,int page, int pageSize, boolean showOnlyActive){
        var list = getEntityManager().createNamedQuery("Sector.findAllInParking", Sector.class)
                .setParameter("parkingId", parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    @Transactional
    public List<Sector> findSectorInParkingWithAvailablePlaces(UUID parkingId,int page, int pageSize, boolean showOnlyActive){
        var list =  getEntityManager().createNamedQuery("Sector.findWithAvailablePlaces", Sector.class)
                .setParameter("parkingId",parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    @Transactional
    public List<Sector> findSectorInParkingBySectorTypes(UUID parkingId, List<Sector.SectorType> sectorTypes, int page, int pageSize, boolean showOnlyActive){
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
     * Removes a sector from the database.
     * This method will not single-handedly remove the sector from database.
     * To remove it you need to additionally update the parking and save changes to database.
     *
     * @param sector Sector to be removed from the database.
     */
    @Transactional
    public void removeSector(Sector sector){
        getEntityManager().remove(sector);
        getEntityManager().flush();
    }

    /**
     * Forces modification of the sector in the database.
     *
     * @param sector Sector to be modified.
     */
    @Transactional
    public void editSector(Sector sector){
        getEntityManager().merge(sector);
        getEntityManager().flush();
    }

    /**
     * Forces a refresh on all elements in the list.
     *
     * @param list List of the Sectors to be refreshed.
     */
    @Transactional
    protected void refreshAllSectors(List<Sector> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(getEntityManager()::refresh);
        }
    }
}
