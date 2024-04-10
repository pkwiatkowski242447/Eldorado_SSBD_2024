package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public Optional<Parking> find(UUID id) {
        return super.find(id);
    }

    @Override
    @Transactional
    public List<Parking> findAll() {
        return super.findAll();
    }

    @Transactional
    public List<Parking> findAllWithPagination(int page, int pageSize, boolean showOnlyActive){
        return getEntityManager().createNamedQuery("Parking.findAll")
                .setFirstResult(page * pageSize)
                .setParameter("showOnlyActive", showOnlyActive)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    public List<Parking> findParkingsBySectorTypes(List<Sector.SectorType> sectorTypes, int page, int pageSize, boolean showOnlyActive){
        return getEntityManager().
                createNamedQuery("Parking.findBySectorTypes")
                .setParameter("sectorTypes", sectorTypes)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    public List<Parking> findParkingWithAvailablePlaces( int page, int pageSize, boolean showOnlyActive){
        return getEntityManager().
                createNamedQuery("Parking.findWithAvailablePlaces")
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
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
    public Optional<Sector> findSectorById(UUID id){
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
        return getEntityManager().createNamedQuery("Sector.findAllInParking")
                .setParameter("parkingId", parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    public List<Sector> findSectorInParkingWithAvailablePlaces(UUID parkingId,int page, int pageSize, boolean showOnlyActive){
        return  getEntityManager().createNamedQuery("Sector.findWithAvailablePlaces")
                .setParameter("parkingId",parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    public List<Sector> findSectorInParkingBySectorTypes(UUID parkingId,int page, int pageSize, boolean showOnlyActive){
        return  getEntityManager().createNamedQuery("Sector.findBySectorTypes")
                .setParameter("parkingId",parkingId)
                .setParameter("showOnlyActive", showOnlyActive)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Transactional
    public void removeSector(Sector sector){
        //TODO parking nie może być nullem
        Parking parking = find(sector.getParking().getId()).orElse(null);
        parking.deleteSector(sector.getName());
        getEntityManager().remove(sector);
        getEntityManager().flush();
        edit(parking);
    }

    @Transactional
    public void editSector(Sector sector){
        getEntityManager().merge(sector);
        getEntityManager().flush();
    }
}
