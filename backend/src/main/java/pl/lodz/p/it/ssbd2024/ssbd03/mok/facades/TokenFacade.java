package pl.lodz.p.it.ssbd2024.ssbd03.mok.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AbstractFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.config.dbconfig.DatabaseConfigConstants;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
public class TokenFacade extends AbstractFacade<Token> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    public TokenFacade() {
        super(Token.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void create(Token entity) {
        super.create(entity);
    }

    @Override
    public void edit(Token entity) {
        super.edit(entity);
    }

    @Override
    public void remove(Token entity) {
        super.remove(entity);
    }

    @Override
    public Optional<Token> find(UUID id) {
        return super.find(id);
    }

    @Override
    public List<Token> findAll() {
        return super.findAll();
    }

    @Override
    public int count() {
        return super.count();
    }

    public Optional<Token> findByTypeAndAccount(Token.TokenType tokenType, UUID accountId) {
        try{
            TypedQuery<Token> query = getEntityManager()
                    .createNamedQuery("Token.findByTypeAndAccount", Token.class)
                    .setParameter("tokenType", tokenType)
                    .setParameter("accountId", accountId);
            return Optional.of(query.getSingleResult());

        } catch(PersistenceException e) {
            return Optional.empty();

        }

    }
    public Optional<Token> findByTokenValue(String tokenValue) {
        try{
            TypedQuery<Token> query = getEntityManager()
                    .createNamedQuery("Token.findByTokenValue", Token.class)
                    .setParameter("tokenValue", tokenValue);
            return Optional.of(query.getSingleResult());

        } catch(PersistenceException e) {
            return Optional.empty();

        }

    }
    public List<Token> findByTokenType(Token.TokenType tokenType) {
        try {
            TypedQuery<Token> query = getEntityManager()
                .createNamedQuery("Token.findByTokenType", Token.class)
                .setParameter("tokenType", tokenType);
            var list = query.getResultList();
            refreshAll(list);
            return list;
        } catch (PersistenceException exception) {
            log.error(exception.getMessage());
            return new ArrayList<>();
        }
    }

    public void removeByAccount(UUID accountId) {
        getEntityManager().createNamedQuery("Token.removeByAccount")
                .setParameter("accountId", accountId)
                .executeUpdate();
    }

    public void removeByTypeAndAccount(Token.TokenType tokenType, UUID accountId) {
        getEntityManager().createNamedQuery("Token.removeByTypeAndAccount")
                .setParameter("tokenType", tokenType)
                .setParameter("accountId", accountId)
                .executeUpdate();
    }

}
