package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.ParkingEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ParkingEventFacade extends AbstractFacade<ParkingEvent> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    public ParkingEventFacade() {
        super(ParkingEvent.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void create(ParkingEvent entity) {
        super.create(entity);
    }

    @Override
    public void edit(ParkingEvent entity) {
        super.edit(entity);
    }

    @Override
    public void remove(ParkingEvent entity) {
        super.remove(entity);
    }

    @Override
    public Optional<ParkingEvent> find(UUID id) {
        return super.find(id);
    }

    @Override
    public List<ParkingEvent> findAll() {
        return super.findAll();
    }

    @Override
    public int count() {
        return super.count();
    }
}
