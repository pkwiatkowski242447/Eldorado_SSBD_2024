package pl.lodz.p.it.ssbd2024.ssbd03.commons;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Basic facade used as a basis for all facade implementations. Includes all basic CRUD methods.
 *
 * @param <T> Class of the entity which the facade will be used for.
 */
public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    /**
     * Constructs the facade and sets the class of the used entity.
     *
     * @param entityClass Class of the used entity.
     */
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Retrieves an entity manager.
     *
     * @return Entity manager associated with the facade.
     */
    protected abstract EntityManager getEntityManager();

    /**
     * Persists a new entity to the database.
     *
     * @param entity Entity to be persisted.
     */
    protected void create(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    /**
     * Forces modification of the entity in the database.
     *
     * @param entity Entity to be modified.
     */
    protected void edit(T entity) {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    /**
     * Removes an entity from the database.
     *
     * @param entity Entity to be removed from the database.
     */
    protected void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().flush();
    }

    /**
     * Retrieves an entity by the ID.
     *
     * @param id ID of the entity to be retrieved.
     * @return If an entity with the given ID was found returns an Optional containing the entity, otherwise returns an empty Optional.
     */
    protected Optional<T> find(UUID id) {
        return Optional.ofNullable(getEntityManager().find(entityClass, id));
    }

    /**
     * Retrieves an entity by the ID and forces its refresh.
     *
     * @param id ID of the entity to be retrieved.
     * @return If an entity with the given ID was found returns an Optional containing the entity, otherwise returns an empty Optional.
     */
    protected Optional<T> findAndRefresh(UUID id) {
        Optional<T> optEntity = Optional.ofNullable(this.getEntityManager().find(entityClass, id));
        optEntity.ifPresent(t -> getEntityManager().refresh(t));
        return optEntity;
    }

    /**
     * Retrieves all entities.
     *
     * @return List containing all entities.
     */
    protected List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        List<T> list = getEntityManager().createQuery(cq).getResultList();
        refreshAll(list);
        return list;
    }

    /**
     * Forces a refresh on all elements in the list.
     *
     * @param list List of the entities to be refreshed.
     */
    protected void refreshAll(List<T> list) {
        if (list != null && !list.isEmpty()) {
            list.forEach(getEntityManager()::refresh);
        }
    }

    /**
     * Counts the number of the entities in the database.
     *
     * @return Number of entities in the database.
     */
    protected int count() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
