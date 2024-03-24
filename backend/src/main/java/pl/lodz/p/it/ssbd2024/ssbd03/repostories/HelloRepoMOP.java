package pl.lodz.p.it.ssbd2024.ssbd03.repostories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity2;

@Repository
public class HelloRepoMOP {

    private final static Logger LOGGER = LoggerFactory.getLogger(HelloRepoMOP.class);

    @PersistenceContext(unitName= DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    @Transactional(DatabaseConfigConstants.TXM_MOP)
    public void save(HelloEntity2 ent) {
        LOGGER.info("USING PU: " + entityManager.getEntityManagerFactory().getProperties().get("hibernate.ejb.persistenceUnitName"));
        entityManager.persist(ent);
    }
}
