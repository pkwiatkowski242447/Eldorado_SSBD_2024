package pl.lodz.p.it.ssbd2024.ssbd03.mop.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mop.Parking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ParkingFacade extends AbstractFacade<Parking> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ParkingFacade.class);

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
    public void edit(Parking entity) {
        super.edit(entity);
    }

    @Override
    public void remove(Parking entity) {
        super.remove(entity);
    }

    @Override
    public Optional<Parking> find(UUID id) {
        return super.find(id);
    }

    @Override
    public List<Parking> findAll() {
        return super.findAll();
    }

    @Override
    public int count() {
        return super.count();
    }
}
