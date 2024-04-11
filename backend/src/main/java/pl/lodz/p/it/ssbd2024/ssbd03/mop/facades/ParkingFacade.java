package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Sector;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
public class ParkingFacade extends AbstractFacade<Parking> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    public ParkingFacade() {
        super(Parking.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    @Transactional
    public void create(Parking entity) {
        super.create(entity);
    }

    @Override
    @Transactional
    public void edit(Parking entity) {
        super.edit(entity);
    }

    @Override
    @Transactional
    public Optional<Parking> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    @Override
    @Transactional
    protected Optional<Parking> find(UUID id) {
        return super.find(id);
    }

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

    @Override
    @Transactional
    public void remove(Parking entity) {
        super.remove(entity);
    }

    @Override
    @Transactional
    public int count() {
        return super.count();
    }


    // -- SECTORS --

    @Transactional
    protected Optional<Sector> findSectorById(UUID id){
        return Optional.ofNullable(getEntityManager().find(Sector.class, id));
    }

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
    public List<Sector> findSectorInParkingBySectorTypes(UUID parkingId,int page, int pageSize, boolean showOnlyActive){
        var list = getEntityManager().createNamedQuery("Sector.findBySectorTypes", Sector.class)
                .setParameter("parkingId",parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        refreshAllSectors(list);
        return list;
    }

    //This method will not single-handedly remove the sector from database
    //To remove it you need to additionally update the parking and save changes to database
    @Transactional
    public void removeSector(Sector sector){
        getEntityManager().remove(sector);
        getEntityManager().flush();
    }

    @Transactional
    public void editSector(Sector sector){
        getEntityManager().merge(sector);
        getEntityManager().flush();
    }

    @Transactional
    protected void refreshAllSectors(List<Sector> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(getEntityManager()::refresh);
        }
    }
}
