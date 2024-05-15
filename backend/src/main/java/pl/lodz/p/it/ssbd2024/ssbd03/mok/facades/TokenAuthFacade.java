package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage Token Entities in the database.
 *
 * @see Token
 */
@Slf4j
@Repository
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
public class TokenAuthFacade extends AbstractFacade<Token> {

    /**
     * Autowired entityManager used for managing entities.
     */
    @PersistenceContext(unitName = DatabaseConfigConstants.AUTH_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public TokenAuthFacade() {
        super(Token.class);
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
     * Persists a new Token in the database.
     *
     * @param entity Token to be persisted.
     */
    @Override
    public void create(Token entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Retrieves a Token by the ID.
     *
     * @param id ID of the Token to be retrieved.
     * @return If Token with the given ID was found returns an Optional containing the Token, otherwise returns an empty Optional.
     */
    @Override
    public Optional<Token> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Retrieves a Token based on the Account associated with the Token and the Token Type.
     *
     * @param tokenType Type of the token to be found.
     * @param accountId ID of the associated Account.
     * @return If found returns Optional containing the Token, otherwise returns Empty Optional.
     */
    public Optional<Token> findByTypeAndAccount(Token.TokenType tokenType, UUID accountId) {
        try {
            TypedQuery<Token> query = getEntityManager()
                    .createNamedQuery("Token.findByTypeAndAccount", Token.class)
                    .setParameter("tokenType", tokenType)
                    .setParameter("accountId", accountId);
            return Optional.of(query.getSingleResult());

        } catch (PersistenceException e) {
            return Optional.empty();
        }
    }

    /**
     * Removes a Token from the database.
     *
     * @param entity Token to be removed from the database.
     */
    @Override
    public void remove(Token entity) {
        super.remove(entity);
    }
}