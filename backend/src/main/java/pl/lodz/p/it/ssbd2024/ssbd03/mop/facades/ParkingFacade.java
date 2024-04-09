package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Transactional
    public void removeSector(Sector sector){
        //TODO parking nie może być nullem
        Parking parking = find(sector.getParking().getId()).orElse(null);
        parking.deleteSector(sector.getName());
        getEntityManager().remove(sector);
        edit(parking);
    }

    @Override
    @Transactional
    public Optional<Parking> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    @Override
    @Transactional
    public void remove(Parking entity) {
        super.remove(entity);
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

    @Override
    @Transactional
    public int count() {
        return super.count();
    }
}
