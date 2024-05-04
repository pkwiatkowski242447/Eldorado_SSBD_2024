package pl.lodz.p.it.ssbd2024.ssbd03.unit.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountCreationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations.AccountService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AccountServiceMockTest {

    @Mock
    private AccountMOKFacade accountMOKFacade;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AccountService accountService;

    @Test
    void registerClientTest() throws AccountCreationException {
        String password = "P@ssw0rd!";

        Mockito.doNothing().when(accountMOKFacade).create(Mockito.any(Account.class));
        Mockito.when(encoder.encode(password)).thenReturn(new BCryptPasswordEncoder().encode(password));

        Account account = accountService.registerClient("Testowy", password, "Imie",
                "Nazwisko", "test@example.com", "123123123", "pl");

        assertNotNull(account);
        assertEquals("Testowy", account.getLogin());
        assertNotNull(account.getPassword());
        assertNotEquals(password, account.getPassword());

        Mockito.verify(accountMOKFacade, Mockito.times(1)).create(Mockito.any(Account.class));
    }
}
