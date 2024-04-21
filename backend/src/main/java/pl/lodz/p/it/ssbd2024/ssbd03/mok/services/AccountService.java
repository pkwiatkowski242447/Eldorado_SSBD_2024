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
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
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

    @Transactional(propagation = Propagation.REQUIRED)
    public void blockAccount(UUID id) throws AccountNotFoundException, AccountAlreadyBlockedException {
        Account account = accountFacade.findAndRefresh(id).orElseThrow(AccountNotFoundException::new);
        if (account.getBlocked()) {
            throw new AccountAlreadyBlockedException("This account is already blocked");
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.error("TODO SECURITY CONTEXT");
            ///TODO Konto z ktorego wykonywana jest czynność nie jest kontem ktore jest blokowane
        }

        account.setBlocked(true);
        account.setBlockedTime(LocalDateTime.now());
        accountFacade.edit(account);

        ///TODO wyslanie powiadomienia email
        ///TODO obsluga proby zablokowania konta użytkownika przez więcej niż 1 administratora???
    }
}
