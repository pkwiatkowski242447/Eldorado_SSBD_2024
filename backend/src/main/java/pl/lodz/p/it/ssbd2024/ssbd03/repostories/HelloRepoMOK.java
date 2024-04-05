package pl.lodz.p.it.ssbd2024.ssbd03.repostories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity;

@Repository
public class HelloRepoMOK {

    @PersistenceContext(unitName=DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    @Transactional
    public void save(HelloEntity ent) {
        entityManager.persist(ent);
    }
}
