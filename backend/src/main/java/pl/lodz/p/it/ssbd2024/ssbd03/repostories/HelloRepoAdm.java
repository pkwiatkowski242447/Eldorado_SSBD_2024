package pl.lodz.p.it.ssbd2024.ssbd03.repostories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity;

@Repository
public class HelloRepoAdm {

    @PersistenceContext(unitName=DatabaseConfigConstants.ADMIN_PU)
    private EntityManager entityManager;

    @Transactional("transactionManagerAdmin")
    public void save(HelloEntity ent) {
        entityManager.persist(ent);
    }
}
