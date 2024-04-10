package pl.lodz.p.it.ssbd2024.ssbd03.commons;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    protected void create(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    protected void edit(T entity) {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    protected void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().flush();
    }

    protected Optional<T> find(UUID id) {
        return Optional.ofNullable(getEntityManager().find(entityClass, id));
    }

    // Ta metoda dodatkowo wykonuje refresh(), czyli wymusza wczytanie z bazy aktualnych danych obiektu
    protected Optional<T> findAndRefresh(UUID id) {
        Optional<T> optEntity = find(id);
        optEntity.ifPresent(t -> getEntityManager().refresh(t));
        return optEntity;
    }

    protected List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    protected int count() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
