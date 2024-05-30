package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.attribute;

import jakarta.annotation.security.DenyAll;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AttributeName;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage AttributeName Entities in the database.
 *
 * @see AttributeName
 */
@Slf4j
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class AttributeNameFacade extends AbstractFacade<AttributeName> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public AttributeNameFacade() {
        super(AttributeName.class);
    }

    /**
     * Retrieves an entity manager.
     *
     * @return Entity manager associated with the facade.
     */
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Persists a new AttributeName in the database.
     *
     * @param attributeName AttributeName to be persisted.
     */
    @Override
    @DenyAll
    protected void create(AttributeName attributeName) throws ApplicationBaseException {
        super.create(attributeName);
    }

    /**
     * Forces the modification of the entity in the database.
     *
     * @param attributeName AttributeName to be modified.
     */
    @Override
    @DenyAll
    protected void edit(AttributeName attributeName) throws ApplicationBaseException {
        super.edit(attributeName);
    }

    /**
     * Removes a AttributeName from the database.
     *
     * @param attributeName AttributeName to be removed from the database.
     */
    @Override
    @DenyAll
    protected void remove(AttributeName attributeName) throws ApplicationBaseException {
        super.remove(attributeName);
    }

    /**
     * Retrieves a AttributeName by the ID and forces its refresh.
     *
     * @param id ID of the AttributeName to be retrieved.
     * @return If AttributeName with the given ID was found returns an Optional containing the AttributeName, otherwise returns an empty Optional.
     */
    @Override
    @DenyAll
    protected Optional<AttributeName> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * This method is used to retrieve all attributes names, including pagination.
     *
     * @param pageNumber Number of the page with attributes names to be retrieved.
     * @param pageSize   Number of attribute name per page.
     * @return List of all attributes names from a specified page, of a given page size.
     * If a persistence exception is thrown, then empty list is returned.
     */
    @DenyAll
    public List<AttributeName> findAllAttributeNamesWithPagination(int pageNumber, int pageSize) throws ApplicationBaseException {
        try {
            TypedQuery<AttributeName> findAllAttributeNames = entityManager.createNamedQuery("AttributeName.findAll", AttributeName.class);
            findAllAttributeNames.setFirstResult(pageNumber * pageSize);
            findAllAttributeNames.setMaxResults(pageSize);
            List<AttributeName> list = findAllAttributeNames.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

//    /**
//     * This method is used to retrieve all attributes names assigned to the account, including pagination.
//     *
//     * @param account Searched attribute names account
//     * @param pageNumber Number of the page with attributes names to be retrieved.
//     * @param pageSize   Number of attribute name per page.
//     * @return List of all attributes names from a specified page, of a given page size.
//     * If a persistence exception is thrown, then empty list is returned.
//     */
//    @DenyAll
//    public List<AttributeName> findByAccountWithPagination(Account account, int pageNumber, int pageSize) throws ApplicationBaseException {
//        try {
//            TypedQuery<AttributeName> findAllAttributeNames = entityManager.createNamedQuery("AttributeName.findByAccount", AttributeName.class);
//            findAllAttributeNames.setFirstResult(pageNumber * pageSize);
//            findAllAttributeNames.setMaxResults(pageSize);
//            findAllAttributeNames.setParameter("account", account);
//            List<AttributeName> list = findAllAttributeNames.getResultList();
//            super.refreshAll(list);
//            return list;
//        } catch (PersistenceException exception) {
//            return new ArrayList<>();
//        }
//    }
}
