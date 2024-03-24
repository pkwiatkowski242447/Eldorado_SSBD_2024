package pl.lodz.p.it.ssbd2024.ssbd03.repostories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.HelloEntity2;

@Repository
public class HelloRepoMOP {

    @PersistenceContext(unitName= DatabaseConfigConstants.MOP_PU)
    private EntityManager entityManager;

    //FIXME problem z uprawnieniami w bazie
    @Transactional(DatabaseConfigConstants.TXM_MOP)
    public void save(HelloEntity2 ent) {
        entityManager.persist(ent);
    }
}
