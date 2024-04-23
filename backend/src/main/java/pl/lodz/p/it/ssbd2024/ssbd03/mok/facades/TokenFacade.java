package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to manage Token Entities in the database.
 *
 * @see Token
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class TokenFacade extends AbstractFacade<Token> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    /**
     * Constructs the facade.
     */
    public TokenFacade() {
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
    public void create(Token entity) {
        super.create(entity);
    }

    /**
     * Forces modification of the Token in the database.
     *
     * @param entity Token to be modified.
     */
    @Override
    public void edit(Token entity) {
        super.edit(entity);
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

    /**
     * Retrieves a Token by the ID.
     *
     * @param id ID of the Token to be retrieved.
     * @return If Token with the given ID was found returns an Optional containing the Token, otherwise returns an empty Optional.
     */
    @Override
    public Optional<Token> find(UUID id) {
        return super.find(id);
    }

    /**
     * Retrieves all Tokens.
     *
     * @return List containing all Tokens.
     */
    @Override
    public List<Token> findAll() {
        return super.findAll();
    }

    /**
     * Counts the number of Tokens in the database.
     *
     * @return Number of Tokens in the database.
     */
    @Override
    public int count() {
        return super.count();
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
     * Retrieves a Token based on its value.
     *
     * @param tokenValue Value of the token to be found.
     * @return If found returns Optional containing the Token, otherwise returns Empty Optional.
     */
    public Optional<Token> findByTokenValue(String tokenValue) {
        try {
            TypedQuery<Token> query = getEntityManager()
                    .createNamedQuery("Token.findByTokenValue", Token.class)
                    .setParameter("tokenValue", tokenValue);
            return Optional.of(query.getSingleResult());

        } catch (PersistenceException e) {
            return Optional.empty();

        }

    }

    /**
     * Removes all Tokens associated with an Account.
     *
     * @param accountId ID of the Account which Token are to be removed.
     */
    public void removeByAccount(UUID accountId) {
        getEntityManager().createNamedQuery("Token.removeByAccount")
                .setParameter("accountId", accountId)
                .executeUpdate();
    }

    /**
     * Removes all Tokens with a given Type associated with an Account.
     *
     * @param tokenType Type of Tokens to be removed.
     * @param accountId ID of the Account which Tokens are to be removed.
     */
    public void removeByTypeAndAccount(Token.TokenType tokenType, UUID accountId) {
        getEntityManager().createNamedQuery("Token.removeByTypeAndAccount")
                .setParameter("tokenType", tokenType)
                .setParameter("accountId", accountId)
                .executeUpdate();
    }

}
