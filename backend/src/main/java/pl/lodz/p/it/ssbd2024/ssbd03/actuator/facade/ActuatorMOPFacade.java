package pl.lodz.p.it.ssbd2024.ssbd03.actuator.facade;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;

import java.util.Objects;

@Slf4j
@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 5)
public class ActuatorMOPFacade extends AbstractFacade<String> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;


    public ActuatorMOPFacade() {
        super(String.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public boolean checkConnection() {
        return Objects.nonNull(entityManager.createNativeQuery("SELECT 1").getSingleResult());
    }
}
