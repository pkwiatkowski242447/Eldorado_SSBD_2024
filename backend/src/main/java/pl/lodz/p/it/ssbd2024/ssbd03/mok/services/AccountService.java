package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountCreationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;

import java.util.UUID;

@Slf4j
@Service
public class AccountService {

    private final AccountMOKFacade accountFacade;
    private final PasswordEncoder passwordEncoder;

    private final MailProvider mailProvider;

    @Autowired
    public AccountService(AccountMOKFacade accountFacade,
                          PasswordEncoder passwordEncoder, MailProvider mailProvider) {
        this.accountFacade = accountFacade;
        this.passwordEncoder = passwordEncoder;
        this.mailProvider = mailProvider;
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
     * Method for blocking an account by its UUID.
     *
     * @param id Account identifier
     * @throws AccountNotFoundException Threw when there is no account with given login.
     * @throws AccountAlreadyBlockedException Threw when the account is already blocked.
     * @throws IllegalOperationException Threw when user try to block their own account.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void blockAccount(UUID id) throws AccountNotFoundException, AccountAlreadyBlockedException, IllegalOperationException {
        Account account = accountFacade.findAndRefresh(id).orElseThrow(AccountNotFoundException::new);
        if (account.getBlocked()) {
            throw new AccountAlreadyBlockedException("This account is already blocked");
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
            SecurityContextHolder.getContext().getAuthentication().getName().equals(account.getLogin())) {
            log.error("You cannot block your own account!");
            throw new IllegalOperationException("You cannot block your own account!");
        }

        account.setBlocked(true);
        // When admin blocks the account property blockedTime is not set
        accountFacade.edit(account);

        // Sending information email
        mailProvider.sendBlockAccountInfoEmail(account.getName(), account.getLastname(), account.getEmail());
        ///TODO obsluga bledu dla proby zablokowania konta użytkownika przez więcej niż 1 administratora???
    }
}
