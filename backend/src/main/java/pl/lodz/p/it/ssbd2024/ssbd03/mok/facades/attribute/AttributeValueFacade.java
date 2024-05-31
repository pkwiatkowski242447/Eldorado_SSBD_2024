package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.attribute;

import jakarta.annotation.security.RolesAllowed;
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
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AttributeValue;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage AttributeValue Entities in the database.
 *
 * @see AttributeValue
 */
@Slf4j
@Repository
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class AttributeValueFacade extends AbstractFacade<AttributeValue> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public AttributeValueFacade() {
        super(AttributeValue.class);
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
     * Persists a new AttributeValue in the database.
     *
     * @param attributeValue AttributeValue to be persisted.
     */
    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    public void create(AttributeValue attributeValue) throws ApplicationBaseException {
        super.create(attributeValue);
    }

    /**
     * Forces the modification of the entity in the database.
     *
     * @param attributeValue AttributeValue to be modified.
     */
    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    public void edit(AttributeValue attributeValue) throws ApplicationBaseException {
        super.edit(attributeValue);
    }

    /**
     * Removes a AttributeValue from the database.
     *
     * @param attributeValue AttributeValue to be removed from the database.
     */
    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    public void remove(AttributeValue attributeValue) throws ApplicationBaseException {
        super.remove(attributeValue);
    }

    /**
     * Retrieves a AttributeValue by the ID and forces its refresh.
     *
     * @param id ID of the AttributeValue to be retrieved.
     * @return If AttributeValue with the given ID was found returns an Optional containing the AttributeValue, otherwise returns an empty Optional.
     */
    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    public Optional<AttributeValue> findAndRefresh(UUID id) throws ApplicationBaseException {
        return super.findAndRefresh(id);
    }

    /**
     * This method is used to retrieve all attributes values, including pagination.
     *
     * @param pageNumber Number of the page with attributes values to be retrieved.
     * @param pageSize   Number of attribute value per page.
     * @return List of all attributes values from a specified page, of a given page size.
     * If a persistence exception is thrown, then empty list is returned.
     * @note. Accounts are be default ordered (in the returned list) by the login.
     */
    @RolesAllowed({Authorities.MANAGE_OWN_ATTRIBUTES, Authorities.MANAGE_ATTRIBUTES})
    public List<AttributeValue> findByAttributeName(String attributeName, int pageNumber, int pageSize)
            throws ApplicationBaseException {
        try {
            TypedQuery<AttributeValue> findAllAttributeValues = entityManager.createNamedQuery("AttributeValue.findByAttributeName", AttributeValue.class);
            findAllAttributeValues.setFirstResult(pageNumber * pageSize);
            findAllAttributeValues.setMaxResults(pageSize);
            findAllAttributeValues.setParameter("attributeName", attributeName);
            List<AttributeValue> list = findAllAttributeValues.getResultList();
            super.refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }
}
