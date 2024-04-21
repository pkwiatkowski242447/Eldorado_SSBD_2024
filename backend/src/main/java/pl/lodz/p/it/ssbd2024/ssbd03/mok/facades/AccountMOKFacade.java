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
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AccountMOKFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = DatabaseConfigConstants.MOK_PU)
    private EntityManager entityManager;

    public AccountMOKFacade() {
        super(Account.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    // C - create methods

    @Override
    public void create(Account account) {
        super.create(account);
    }

    // R - read methods

    @Override
    public Optional<Account> find(UUID id) {
        return super.find(id);
    }

    @Override
    public Optional<Account> findAndRefresh(UUID id) {
        return super.findAndRefresh(id);
    }

    @Override
    public List<Account> findAll() {
        return super.findAll();
    }

    public Optional<List<Account>> findAllActiveAccountsWithPagination(int pageNumber, int pageSize) {
        try {
            TypedQuery<Account> findAllAccounts = entityManager.createNamedQuery("Account.findAllAccountsByActive", Account.class);
            findAllAccounts.setFirstResult(pageNumber * pageSize);
            findAllAccounts.setMaxResults(pageSize);
            findAllAccounts.setParameter("active", true);
            var list = findAllAccounts.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllInactiveAccountsWithPagination(int pageNumber, int pageSize) {
        try {
            TypedQuery<Account> findAllAccounts = entityManager.createNamedQuery("Account.findAllAccountsByActive", Account.class);
            findAllAccounts.setFirstResult(pageNumber * pageSize);
            findAllAccounts.setMaxResults(pageSize);
            findAllAccounts.setParameter("active", false);
            var list = findAllAccounts.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllActiveAccountsWithGivenUserLevelWithPagination(int pageNumber, int pageSize, Class<? extends UserLevel> userLevel) {
        try {
            TypedQuery<Account> findAllActiveAccountsByUserLevelQuery = entityManager.createNamedQuery("Account.findAccountsByUserLevelAndActive", Account.class);
            findAllActiveAccountsByUserLevelQuery.setFirstResult(pageNumber * pageSize);
            findAllActiveAccountsByUserLevelQuery.setMaxResults(pageSize);
            findAllActiveAccountsByUserLevelQuery.setParameter("userLevel", userLevel);
            var list = findAllActiveAccountsByUserLevelQuery.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<Account> findByLogin(String login) {
        try {
            TypedQuery<Account> findAccountByLogin = entityManager.createNamedQuery("Account.findByLogin", Account.class);
            findAccountByLogin.setParameter("login", login);
            return Optional.of(findAccountByLogin.getSingleResult());
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<Account> findByEmail(String email) {
        try {
            TypedQuery<Account> findAccountByEmail = entityManager.createNamedQuery("Account.findAccountByEmail", Account.class);
            findAccountByEmail.setParameter("email", email);
            return Optional.of(findAccountByEmail.getSingleResult());
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllAccountsMatchingLoginWithPagination(int pageNumber, int pageSize, String login, boolean active) {
        try {
            TypedQuery<Account> findAllAccountsMatchingLogin = entityManager.createNamedQuery("Account.findAllAccountsMatchingGivenLogin", Account.class);
            findAllAccountsMatchingLogin.setFirstResult(pageNumber * pageSize);
            findAllAccountsMatchingLogin.setMaxResults(pageSize);
            findAllAccountsMatchingLogin.setParameter("login", login);
            findAllAccountsMatchingLogin.setParameter("active", active);
            var list = findAllAccountsMatchingLogin.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<List<Account>> findAllAccountsMarkedForDeletion(long amount, TimeUnit timeUnit) {
        try {
            TypedQuery<Account> findAllAccountsMarkedForDeletion = entityManager.createNamedQuery("Account.findAllAccountsMarkedForDeletion", Account.class);
            findAllAccountsMarkedForDeletion.setParameter("timestamp", LocalDateTime.now().minus(amount, timeUnit.toChronoUnit()));
            var list = findAllAccountsMarkedForDeletion.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllAccountsByBlocked(int pageNumber, int pageSize, boolean blocked) {
        try {
            TypedQuery<Account> findAllBlockedAccounts = entityManager.createNamedQuery("Account.findAllAccountsByBlockedInAscOrder", Account.class);
            findAllBlockedAccounts.setFirstResult(pageNumber * pageSize);
            findAllBlockedAccounts.setMaxResults(pageSize);
            findAllBlockedAccounts.setParameter("blocked", blocked);
            var list = findAllBlockedAccounts.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllBlockedAccountsThatWereBlockedByAdminWithPagination(int pageNumber, int pageSize) {
        try {
            TypedQuery<Account> findAllAccountsBlockedByAdminQuery = entityManager.createNamedQuery("Account.findAllBlockedAccountsThatWereBlockedByAdmin", Account.class);
            findAllAccountsBlockedByAdminQuery.setFirstResult(pageNumber * pageSize);
            findAllAccountsBlockedByAdminQuery.setMaxResults(pageSize);
            var list = findAllAccountsBlockedByAdminQuery.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimesWithPagination(int pageNumber, int pageSize) {
        try {
            TypedQuery<Account> findAllAccountsBlockedByLoginIncorrectlyCertainAmountOfTimesQuery = entityManager
                    .createNamedQuery("Account.findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes", Account.class);
            findAllAccountsBlockedByLoginIncorrectlyCertainAmountOfTimesQuery.setFirstResult(pageNumber * pageSize);
            findAllAccountsBlockedByLoginIncorrectlyCertainAmountOfTimesQuery.setMaxResults(pageSize);
            var list = findAllAccountsBlockedByLoginIncorrectlyCertainAmountOfTimesQuery.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllActiveAccountsWithUnverifiedEmailWithPagination(int pageNumber, int pageSize) {
        try {
            TypedQuery<Account> findAllAccountsWithUnverifiedEmailQuery = entityManager.createNamedQuery("Account.findAllAccountsByVerifiedAndActiveInAscOrder", Account.class);
            findAllAccountsWithUnverifiedEmailQuery.setFirstResult(pageNumber * pageSize);
            findAllAccountsWithUnverifiedEmailQuery.setMaxResults(pageSize);
            findAllAccountsWithUnverifiedEmailQuery.setParameter("verified", false);
            findAllAccountsWithUnverifiedEmailQuery.setParameter("active", true);
            var list = findAllAccountsWithUnverifiedEmailQuery.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllInactiveAccountsWithUnverifiedEmailWithPagination(int pageNumber, int pageSize) {
        try {
            TypedQuery<Account> findAllAccountsWithUnverifiedEmailQuery = entityManager.createNamedQuery("Account.findAllAccountsByVerifiedAndActiveInAscOrder", Account.class);
            findAllAccountsWithUnverifiedEmailQuery.setFirstResult(pageNumber * pageSize);
            findAllAccountsWithUnverifiedEmailQuery.setMaxResults(pageSize);
            findAllAccountsWithUnverifiedEmailQuery.setParameter("verified", false);
            findAllAccountsWithUnverifiedEmailQuery.setParameter("active", false);
            var list = findAllAccountsWithUnverifiedEmailQuery.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination(String login,
                                                                                                                String firstName,
                                                                                                                String lastName,
                                                                                                                boolean active,
                                                                                                                boolean order,
                                                                                                                int pageSize,
                                                                                                                int pageNumber) {
        try {
            TypedQuery<Account> findAllAccountsMatchingCriteriaQuery;
            if (order) {
                findAllAccountsMatchingCriteriaQuery = entityManager.createNamedQuery("Account.findAccountsByActiveAndMatchingUserFirstNameOrUserLastNameAndLoginInAscendingOrder", Account.class);
            } else {
                findAllAccountsMatchingCriteriaQuery = entityManager.createNamedQuery("Account.findAccountsByActiveAndMatchingUserFirstNameOrUserLastNameAndLoginInDescendingOrder", Account.class);
            }
            findAllAccountsMatchingCriteriaQuery.setFirstResult(pageNumber * pageSize);
            findAllAccountsMatchingCriteriaQuery.setMaxResults(pageSize);
            findAllAccountsMatchingCriteriaQuery.setParameter("login", login);
            findAllAccountsMatchingCriteriaQuery.setParameter("firstName", firstName);
            findAllAccountsMatchingCriteriaQuery.setParameter("lastName", lastName);
            findAllAccountsMatchingCriteriaQuery.setParameter("active", active);
            var list = findAllAccountsMatchingCriteriaQuery.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<List<Account>> findAllAccountsWithoutRecentActivityWithPagination(int pageSize, int pageNumber, LocalDateTime lastSuccessfulLogin) {
        try {
            TypedQuery<Account> findAllAccountsWithoutRecentActivityQuery = entityManager.createNamedQuery("Account.findAccountsWithoutAnyActivityFrom", Account.class);
            findAllAccountsWithoutRecentActivityQuery.setFirstResult(pageNumber * pageSize);
            findAllAccountsWithoutRecentActivityQuery.setMaxResults(pageSize);
            findAllAccountsWithoutRecentActivityQuery.setParameter("lastSuccessfulLoginTime", lastSuccessfulLogin);
            var list = findAllAccountsWithoutRecentActivityQuery.getResultList();
            refreshAll(list);
            return Optional.of(list);
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    public Optional<Integer> countAllAccountsWithoutRecentActivityWithPagination(int pageSize, int pageNumber, LocalDateTime lastSuccessfulLogin) {
        try {
            TypedQuery<Integer> countAllAccountsWithoutRecentActivityQuery = entityManager.createNamedQuery("Account.countAccountsWithoutAnyActivityFrom", Integer.class);
            countAllAccountsWithoutRecentActivityQuery.setFirstResult(pageNumber * pageSize);
            countAllAccountsWithoutRecentActivityQuery.setMaxResults(pageSize);
            countAllAccountsWithoutRecentActivityQuery.setParameter("lastSuccessfulLoginTime", lastSuccessfulLogin);
            return Optional.of(countAllAccountsWithoutRecentActivityQuery.getSingleResult());
        } catch (PersistenceException exception) {
            return Optional.empty();
        }
    }

    // U - update methods

    @Override
    public void edit(Account account) {
        super.edit(account);
    }

    // D - delete methods

    @Override
    public void remove(Account account) {
        super.remove(account);
    }
}
