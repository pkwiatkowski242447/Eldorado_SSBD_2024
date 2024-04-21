package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountCreationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;

import java.util.List;

@Service
public class AccountService {

    private final AccountMOKFacade accountFacade;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountMOKFacade accountFacade,
                          PasswordEncoder passwordEncoder) {
        this.accountFacade = accountFacade;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Account registerClient(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws AccountCreationException {
        try {
            Account account = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
            account.setAccountLanguage(language);
            UserLevel clientLevel = new Client();
            clientLevel.setAccount(account);
            account.addUserLevel(clientLevel);

            this.accountFacade.create(account);

            return account;
        } catch (PersistenceException exception) {
            throw new AccountCreationException(exception.getMessage(), exception);
        }
    }

    /**
     * Retrieve Account that match the parameters.
     *
     * @param login      Account's login. A phrase is sought in the logins.
     * @param firstName  Account's owner first name. A phrase is sought in the names.
     * @param lastName   Account's owner last name. A phrase is sought in the last names.
     * @param order
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Transactional
    public List<Account> getAccountsByMatchingLoginFirstNameAndLastName(String login,
                                                                        String firstName,
                                                                        String lastName,
                                                                        boolean order,
                                                                        int pageNumber,
                                                                        int pageSize) {
        return accountFacade.findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination(login, firstName, lastName, order, pageNumber, pageSize);
    }

    @Transactional
    public List<Account> getAllAccounts(int pageNumber, int pageSize) {
        return accountFacade.findAllAccountsWithPagination(pageNumber, pageSize);
    }
}
