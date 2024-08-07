package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
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
@LoggerInterceptor
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
    @RolesAllowed({Authorities.LOGIN, Authorities.REFRESH_SESSION})
    public void create(Token entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Retrieves a Token based on the Account associated with the Token and the Token Type.
     *
     * @param tokenType Type of the token to be found.
     * @param accountId ID of the associated Account.
     * @return If found returns Optional containing the Token, otherwise returns Empty Optional.
     */
    @RolesAllowed({Authorities.LOGIN})
    public Optional<Token> findByTypeAndAccount(Token.TokenType tokenType, UUID accountId) throws ApplicationBaseException {
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
    @RolesAllowed({Authorities.REFRESH_SESSION})
    public Optional<Token> findByTokenValue(String tokenValue) throws ApplicationBaseException {
        try {
            TypedQuery<Token> query = getEntityManager()
                    .createNamedQuery("Token.findByTokenValue", Token.class)
                    .setParameter("tokenValue", tokenValue);
            Token token = query.getSingleResult();
            entityManager.refresh(token);
            return Optional.of(token);
        } catch (PersistenceException e) {
            return Optional.empty();
        }
    }

    /**
     * Removes all Tokens with a given Type associated with an Account.
     *
     * @param tokenType Type of Tokens to be removed.
     * @param accountId ID of the Account which Tokens are to be removed.
     */
    @RolesAllowed({Authorities.LOGIN})
    public void removeByTypeAndAccount(Token.TokenType tokenType, UUID accountId) throws ApplicationBaseException {
        getEntityManager().createNamedQuery("Token.removeByTypeAndAccount")
                .setParameter("tokenType", tokenType)
                .setParameter("accountId", accountId)
                .executeUpdate();
    }

    /**
     * Removes a Token from the database.
     *
     * @param entity Token to be removed from the database.
     */
    @Override
    @RolesAllowed({Authorities.LOGIN, Authorities.REFRESH_SESSION})
    public void remove(Token entity) throws ApplicationBaseException {
        super.remove(entity);
    }
}
