package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountServiceInterface {

    Account registerClient(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws AccountCreationException;
    void registerStaff(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws AccountCreationException;
    void registerAdmin(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws AccountCreationException;

    void blockAccount(UUID id) throws AccountNotFoundException, AccountAlreadyBlockedException, IllegalOperationException;
    void unblockAccount(UUID id) throws AccountNotFoundException, AccountAlreadyUnblockedException;

    Account modifyAccount(Account modifiedAccount) throws AccountNotFoundException;
    void changeEmail(UUID accountId, String newEmail) throws AccountEmailChangeException, AccountNotFoundException;

    boolean activateAccount(String token);
    boolean confirmEmail(String token) throws AccountNotFoundException, AccountEmailNullException, AccountEmailChangeException;

    List<Account> getAccountsByMatchingLoginFirstNameAndLastName(String login, String firstName, String lastName,boolean active, boolean order, int pageNumber, int pageSize);
    List<Account> getAllAccounts(int pageNumber, int pageSize);
    Account getAccountByLogin(String login);

    Optional<Account> getAccountById(UUID id);

    void resendEmailConfirmation() throws AccountNotFoundException, TokenNotFoundException;
}
