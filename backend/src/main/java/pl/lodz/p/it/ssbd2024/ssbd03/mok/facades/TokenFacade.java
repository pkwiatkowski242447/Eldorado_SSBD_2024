package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
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
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Token;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.ArrayList;
import java.util.List;
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
public class TokenFacade extends AbstractFacade<Token> {

    /**
     * Autowired entityManager used for managing entities.
     */
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
    @RolesAllowed({
            Authorities.REGISTER_CLIENT, Authorities.REGISTER_USER, Authorities.RESET_PASSWORD,
            Authorities.CHANGE_OWN_MAIL, Authorities.RESEND_EMAIL_CONFIRMATION_MAIL,
            Authorities.RESTORE_ACCOUNT_ACCESS, Authorities.CHANGE_USER_PASSWORD
    })
    public void create(Token entity) throws ApplicationBaseException {
        super.create(entity);
    }

    /**
     * Forces modification of the Token in the database.
     *
     * @param entity Token to be modified.
     */
    @Override
    // @RolesAllowed(Roles.AUTHENTICATED)
    @DenyAll
    public void edit(Token entity) throws ApplicationBaseException {
        super.edit(entity);
    }

    /**
     * Removes a Token from the database.
     *
     * @param entity Token to be removed from the database.
     */
    @Override
    @RolesAllowed({
            Authorities.CHANGE_USER_PASSWORD, Authorities.CONFIRM_ACCOUNT_CREATION, Authorities.CONFIRM_EMAIL_CHANGE,
            Authorities.CHANGE_OWN_MAIL, Authorities.RESEND_EMAIL_CONFIRMATION_MAIL, Authorities.RESTORE_ACCOUNT_ACCESS,
    })
    public void remove(Token entity) throws ApplicationBaseException {
        super.remove(entity);
    }

    /**
     * Retrieves a Token by the ID.
     *
     * @param id ID of the Token to be retrieved.
     * @return If Token with the given ID was found returns an Optional containing the Token, otherwise returns an empty Optional.
     */
    @Override
    @DenyAll
    public Optional<Token> find(UUID id) throws ApplicationBaseException {
        return super.find(id);
    }

    /**
     * Retrieves all Tokens.
     *
     * @return List containing all Tokens.
     */
    @Override
    // @RolesAllowed({Roles.ADMIN})
    @DenyAll
    public List<Token> findAll() throws ApplicationBaseException {
        return super.findAll();
    }

    /**
     * Counts the number of Tokens in the database.
     *
     * @return Number of Tokens in the database.
     */
    @Override
    // @RolesAllowed({Roles.ADMIN})
    @DenyAll
    public int count() throws ApplicationBaseException {
        return super.count();
    }

    /**
     * Retrieves a Token based on the Account associated with the Token and the Token Type.
     *
     * @param tokenType Type of the token to be found.
     * @param accountId ID of the associated Account.
     * @return If found returns Optional containing the Token, otherwise returns Empty Optional.
     */
    @RolesAllowed({
            Authorities.RESEND_EMAIL_CONFIRMATION_MAIL, Authorities.GET_ADMIN_PASSWORD_RESET_STATUS
    })
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
    @RolesAllowed({
            Authorities.CHANGE_USER_PASSWORD, Authorities.CONFIRM_ACCOUNT_CREATION, Authorities.CONFIRM_EMAIL_CHANGE,
            Authorities.RESTORE_ACCOUNT_ACCESS
    })
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
     * This method is used to retrieve all token of a specified TokenType from the database.
     *
     * @param tokenType Type of the token. All tokens of a given type will be returned.
     *
     * @return List of tokens of the specified token type.
     */
    @DenyAll
    public List<Token> findByTokenType(Token.TokenType tokenType) throws ApplicationBaseException {
        try {
            TypedQuery<Token> query = getEntityManager()
                .createNamedQuery("Token.findByTokenType", Token.class)
                .setParameter("tokenType", tokenType);
            var list = query.getResultList();
            refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            return new ArrayList<>();
        }
    }

    /**
     * Removes all Tokens associated with an Account.
     *
     * @param accountId ID of the Account which Token are to be removed.
     */
    @DenyAll
    public void removeByAccount(UUID accountId) throws ApplicationBaseException {
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
    @RolesAllowed({
            Authorities.RESET_PASSWORD, Authorities.CHANGE_OWN_MAIL, Authorities.RESTORE_ACCOUNT_ACCESS,
            Authorities.CHANGE_USER_PASSWORD
    })
    public void removeByTypeAndAccount(Token.TokenType tokenType, UUID accountId) throws ApplicationBaseException {
        getEntityManager().createNamedQuery("Token.removeByTypeAndAccount")
                .setParameter("tokenType", tokenType)
                .setParameter("accountId", accountId)
                .executeUpdate();
    }
}
