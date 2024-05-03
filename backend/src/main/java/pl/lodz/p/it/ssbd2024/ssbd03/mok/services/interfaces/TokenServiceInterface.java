package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;

public interface TokenServiceInterface {

    String createRegistrationToken(Account account);
    String createEmailConfirmationToken(Account account, String email);
    void removeAccountsEmailConfirmationToken(String token);
}
