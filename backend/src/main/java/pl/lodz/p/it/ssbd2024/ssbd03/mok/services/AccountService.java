package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Client;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.UserLevel;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountCreationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountEmailChangeException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountSameEmailException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountValidationException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages.SAME_EMAIL_EXCEPTION;
import static pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages.VALIDATION_EXCEPTION;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class AccountService {

    private final AccountMOKFacade accountFacade;
    private final PasswordEncoder passwordEncoder;

    private final Validator validator;

    @Autowired
    public AccountService(AccountMOKFacade accountFacade,
                          PasswordEncoder passwordEncoder,
                          Validator validator) {
        this.accountFacade = accountFacade;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

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
    public List<Account> getAccountsByMatchingLoginFirstNameAndLastName(String login,
                                                                        String firstName,
                                                                        String lastName,
                                                                        boolean order,
                                                                        int pageNumber,
                                                                        int pageSize) {
        return accountFacade.findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination(login, firstName, lastName, order, pageNumber, pageSize);
    }

    public List<Account> getAllAccounts(int pageNumber, int pageSize) {
        return accountFacade.findAllAccountsWithPagination(pageNumber, pageSize);
    }

    /**
     * Retrieves from the database account by id.
     *
     * @param id Account's id.
     * @return Returns Optional containing the requested account if found, otherwise returns empty Optional.
     */
    public Optional<Account> getAccountById(UUID id) {
        return accountFacade.findAndRefresh(id);
    }

    /**
     * Changes the e-mail of the specified Account.
     *
     * @param account  Account which the e-mail will be changed.
     * @param newEmail New e-mail address.
     * @throws AccountEmailChangeException Threw if any problem related to the e-mail occurs.
     *                                     Contains a key to an internationalized message.
     *                                     Additionally, if the problem was caused by an incorrect new mail,
     *                                     the cause is set to <code>AccountValidationException</code> which contains more details about the incorrect fields.
     */
    public void changeEmail(Account account, String newEmail) throws AccountEmailChangeException {
        try {
            if (account.getEmail().equals(newEmail)) throw new AccountSameEmailException(SAME_EMAIL_EXCEPTION);

            account.setEmail(newEmail);
            account.setVerified(false);
            var errors = validator.validateObject(account);
            if (errors.hasErrors()) throw new AccountValidationException(VALIDATION_EXCEPTION,
                    errors.getFieldErrors()
                            .stream()
                            .map(v -> new AccountValidationException.FieldConstraintViolation(v.getField(),
                                    Optional.ofNullable(v.getRejectedValue()).orElse(" ").toString(),
                                    Optional.ofNullable(v.getDefaultMessage()).orElse(" ")))
                            .collect(Collectors.toSet()));
            accountFacade.edit(account);
        } catch (AccountValidationException e) {
            //TODO internationalization
            log.error(e.getMessage());
            throw new AccountEmailChangeException(I18n.ACCOUNT_CONSTRAINT_VALIDATION_EXCEPTION, e);
        } catch (AccountSameEmailException e) {
            log.error(e.getMessage());
            throw new AccountEmailChangeException(I18n.ACCOUNT_SAME_EMAIL_EXCEPTION, e);
        } catch (ConstraintViolationException e) {
            log.error(e.getMessage());
            throw new AccountEmailChangeException(I18n.ACCOUNT_EMAIL_COLLISION_EXCEPTION, e);
        }
    }
}
