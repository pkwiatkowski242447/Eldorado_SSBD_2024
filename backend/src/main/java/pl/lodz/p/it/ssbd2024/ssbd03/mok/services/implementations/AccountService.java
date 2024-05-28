package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.implementations;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Roles;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountRestoreAccessException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.AccountUserLevelException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountAlreadyUnblockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountEmailAlreadyTakenException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.conflict.AccountSameEmailException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.integrity.UserLevelMissingException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountEmailNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountEmailNullException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountIdNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword.IncorrectPasswordException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.resetOwnPassword.PasswordPreviouslyUsedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.validation.AccountConstraintViolationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.token.TokenNotValidException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountHistoryDataFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.UserLevelFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AccountServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.MailProvider;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.TokenProvider;

import java.util.*;

/**
 * Service managing Accounts.
 *
 * @see Account
 */
@Slf4j
@Service
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AccountService implements AccountServiceInterface {

    @Value("${mail.account.creation.confirmation.url}")
    private String accountCreationConfirmationUrl;

    @Value("${mail.account.reset.password.url}")
    private String accountPasswordResetUrl;

    @Value("${mail.account.confirm.email.url}")
    private String accountConfirmEmail;

    @Value("${mail.account.restore.access.url}")
    private String restoreAccountAccessURL;

    /**
     * AccountFacade used for operations on account entities.
     */
    private final AccountMOKFacade accountFacade;

    private final AccountHistoryDataFacade historyDataFacade;

    /**
     * PasswordEncoder used for encoding passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * TokenFacade used for operations on token entities.
     */
    private final TokenFacade tokenFacade;

    /**
     * Component used to send e-mail messages to user e-mail address (depending on the actions they perform).
     */
    private final MailProvider mailProvider;

    /**
     * JWTProvider used for operations on TOKENS.
     */
    private final JWTProvider jwtProvider;

    /**
     * TokenProvider component used for generating action tokens.
     */
    private final TokenProvider tokenProvider;

    /**
     * Facade component used to manage user levels in the database.
     */
    private final UserLevelFacade userLevelFacade;

    /**
     * Autowired constructor for the service.
     *
     * @param accountFacade   Facade responsible for users accounts management.
     * @param historyDataFacade    Facade used for inserting information about account modifications to the database.
     * @param passwordEncoder This component is used to generate hashed passwords for user accounts (not to store them in their original form).
     * @param tokenFacade     This facade is responsible for manipulating tokens, used for various, user account related operations.
     * @param mailProvider    This component is used to send e-mail messages to e-mail address of users (where message depends on their actions).
     * @param jwtProvider     This component is used to generate token values for token facade.
     * @param userLevelFacade It is used to create new tokens, remove them, etc.
     */
    @Autowired
    public AccountService(AccountMOKFacade accountFacade,
                          AccountHistoryDataFacade historyDataFacade,
                          PasswordEncoder passwordEncoder,
                          TokenFacade tokenFacade,
                          MailProvider mailProvider,
                          JWTProvider jwtProvider,
                          TokenProvider tokenProvider,
                          UserLevelFacade userLevelFacade) {
        this.accountFacade = accountFacade;
        this.historyDataFacade = historyDataFacade;
        this.passwordEncoder = passwordEncoder;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
        this.jwtProvider = jwtProvider;
        this.tokenProvider = tokenProvider;
        this.userLevelFacade = userLevelFacade;
    }

    // Register user account methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    public Account registerClient(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language)
            throws ApplicationBaseException {
        Account newClientAccount = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
        newClientAccount.setAccountLanguage(language);
        newClientAccount.getPreviousPasswords().add(newClientAccount.getPassword());
        UserLevel clientUserLevel = new Client();
        clientUserLevel.setAccount(newClientAccount);
        newClientAccount.addUserLevel(clientUserLevel);

        this.accountFacade.create(newClientAccount);
        historyDataFacade.create(new AccountHistoryData(newClientAccount,
                OperationType.REGISTRATION,
                accountFacade.findByLogin(SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName())
                        .orElse(null)));

        Token accountActivationToken = tokenProvider.generateAccountActivationToken(newClientAccount);
        tokenFacade.create(accountActivationToken);


        String encodedTokenValue = new String(Base64.getUrlEncoder().encode(accountActivationToken.getTokenValue().getBytes()));
        String confirmationURL = this.accountCreationConfirmationUrl + encodedTokenValue;

        mailProvider.sendRegistrationConfirmEmail(newClientAccount.getName(),
                newClientAccount.getLastname(),
                newClientAccount.getEmail(),
                confirmationURL,
                newClientAccount.getAccountLanguage());

        return newClientAccount;
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    public Account registerStaff(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws ApplicationBaseException {
        Account newStaffAccount = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
        newStaffAccount.setAccountLanguage(language);
        newStaffAccount.getPreviousPasswords().add(newStaffAccount.getPassword());
        UserLevel staffUserLevel = new Staff();
        staffUserLevel.setAccount(newStaffAccount);
        newStaffAccount.addUserLevel(staffUserLevel);

        accountFacade.create(newStaffAccount);
        historyDataFacade.create(new AccountHistoryData(newStaffAccount,
                OperationType.REGISTRATION,
                accountFacade.findByLogin(SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName())
                        .orElse(null)));

        Token accountActivationToken = tokenProvider.generateAccountActivationToken(newStaffAccount);
        tokenFacade.create(accountActivationToken);

        String encodedTokenValue = new String(Base64.getUrlEncoder().encode(accountActivationToken.getTokenValue().getBytes()));
        String confirmationURL = this.accountCreationConfirmationUrl + encodedTokenValue;

        mailProvider.sendRegistrationConfirmEmail(newStaffAccount.getName(),
                newStaffAccount.getLastname(),
                newStaffAccount.getEmail(),
                confirmationURL,
                newStaffAccount.getAccountLanguage());

        return newStaffAccount;
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    public Account registerAdmin(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language) throws ApplicationBaseException {
        Account newAdminAccount = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
        newAdminAccount.setAccountLanguage(language);
        newAdminAccount.getPreviousPasswords().add(newAdminAccount.getPassword());
        UserLevel adminUserLevel = new Admin();
        adminUserLevel.setAccount(newAdminAccount);
        newAdminAccount.addUserLevel(adminUserLevel);

        accountFacade.create(newAdminAccount);
        historyDataFacade.create(new AccountHistoryData(newAdminAccount,
                OperationType.REGISTRATION,
                accountFacade.findByLogin(SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName())
                        .orElse(null)));

        Token accountActivationToken = tokenProvider.generateAccountActivationToken(newAdminAccount);
        tokenFacade.create(accountActivationToken);

        String encodedTokenValue = new String(Base64.getUrlEncoder().encode(accountActivationToken.getTokenValue().getBytes()));
        String confirmationURL = this.accountCreationConfirmationUrl + encodedTokenValue;

        mailProvider.sendRegistrationConfirmEmail(newAdminAccount.getName(),
                newAdminAccount.getLastname(),
                newAdminAccount.getEmail(),
                confirmationURL,
                newAdminAccount.getAccountLanguage());

        return newAdminAccount;
    }

    // Password change methods

    @RolesAllowed({Roles.ANONYMOUS, Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    public void forgetAccountPassword(String userEmail) throws ApplicationBaseException {
        Account account = this.accountFacade.findByEmail(userEmail).orElseThrow(AccountEmailNotFoundException::new);

        if (account.getBlocked()) throw new AccountBlockedException();
        else if (!account.getActive()) throw new AccountNotActivatedException();

        tokenFacade.removeByTypeAndAccount(Token.TokenType.RESET_PASSWORD, account.getId());

        Token passwordResetToken = tokenProvider.generatePasswordResetToken(account);
        this.tokenFacade.create(passwordResetToken);

        String encodedTokenValue = new String(Base64.getUrlEncoder().encode(passwordResetToken.getTokenValue().getBytes()));
        String passwordResetURL = this.accountPasswordResetUrl + encodedTokenValue;

        this.mailProvider.sendPasswordResetEmail(account.getName(),
                account.getLastname(),
                userEmail,
                passwordResetURL,
                account.getAccountLanguage());
    }

    @RolesAllowed({Roles.ANONYMOUS})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class, noRollbackFor = PasswordPreviouslyUsedException.class)
    public void changeAccountPassword(String token, String newPassword) throws ApplicationBaseException {
        String decodedTokenValue = new String(Base64.getUrlDecoder().decode(token.getBytes()));
        Token tokenObject = this.tokenFacade.findByTokenValue(decodedTokenValue).orElseThrow(TokenNotFoundException::new);

        this.tokenFacade.remove(tokenObject);

        if (!jwtProvider.isTokenValid(tokenObject.getTokenValue(), tokenObject.getAccount()))
            throw new TokenNotValidException(I18n.TOKEN_NOT_VALID_EXCEPTION);

        Account account = this.accountFacade.findAndRefresh(tokenObject.getAccount().getId()).orElseThrow(AccountIdNotFoundException::new);
        if (account.getBlocked()) throw new AccountBlockedException();
        else if (!account.getActive()) throw new AccountNotActivatedException();

        for (String passwordHash : account.getPreviousPasswords()) {
            if (passwordEncoder.matches(newPassword, passwordHash)) throw new PasswordPreviouslyUsedException();
        }

        account.getPreviousPasswords().add(newPassword);
        account.setPassword(this.passwordEncoder.encode(newPassword));

        this.accountFacade.edit(account);
        historyDataFacade.create(new AccountHistoryData(account,
                OperationType.PASSWORD_CHANGE,
                accountFacade.findByLogin(SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName())
                        .orElse(null)));
    }

    @Override
    @RolesAllowed({Roles.AUTHENTICATED})
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ApplicationBaseException.class)
    public void changePasswordSelf(String oldPassword, String newPassword, String login) throws ApplicationBaseException {

        String newPasswordEncoded = passwordEncoder.encode(newPassword);

        Account account = accountFacade.findByLogin(login).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        String passwordFromDatabase = account.getPassword();

        if (!passwordEncoder.matches(oldPassword, passwordFromDatabase)) {
            throw new IncorrectPasswordException();
        }

        for (String hashedPassword : account.getPreviousPasswords()) {
            if (passwordEncoder.matches(newPassword, hashedPassword)) {
                throw new PasswordPreviouslyUsedException();
            }
        }

        account.setPassword(newPasswordEncoded);

        accountFacade.edit(account);
        historyDataFacade.create(new AccountHistoryData(account,
                OperationType.PASSWORD_CHANGE,
                accountFacade.findByLogin(SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName())
                        .orElse(null)));
    }

    // Block & unblock account method

    @Override
    @RolesAllowed({Roles.ADMIN})
    public void blockAccount(UUID id) throws ApplicationBaseException {
        Account account = accountFacade.findAndRefresh(id).orElseThrow(AccountNotFoundException::new);
        if (account.getBlocked() && account.getBlockedTime() == null) {
            throw new AccountAlreadyBlockedException();
        }

        account.blockAccount(true);

        accountFacade.edit(account);
        historyDataFacade.create(new AccountHistoryData(account,
                OperationType.BLOCK,
                accountFacade.findByLogin(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                        .orElse(null)));

        // Sending information email
        mailProvider.sendBlockAccountInfoEmail(account.getName(), account.getLastname(),
                account.getEmail(), account.getAccountLanguage(), true);
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    public void unblockAccount(UUID id) throws ApplicationBaseException {
        Account account = accountFacade.findAndRefresh(id).orElseThrow(AccountNotFoundException::new);
        if (!account.getBlocked()) {
            throw new AccountAlreadyUnblockedException();
        }

        account.unblockAccount();

        accountFacade.edit(account);
        historyDataFacade.create(new AccountHistoryData(account,
                OperationType.UNBLOCK,
                accountFacade.findByLogin(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                        .orElse(null)));

        // Sending information email
        mailProvider.sendUnblockAccountInfoEmail(account.getName(), account.getLastname(),
                account.getEmail(), account.getAccountLanguage());
    }

    // Modify account methods

    @Override
    @RolesAllowed({Roles.AUTHENTICATED})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AccountConstraintViolationException.class)
    public Account modifyAccount(Account modifiedAccount, String userLogin) throws ApplicationBaseException {
        Account foundAccount = accountFacade.findByLogin(userLogin).orElseThrow(AccountNotFoundException::new);

        if (!modifiedAccount.getVersion().equals(foundAccount.getVersion())) {
            throw new ApplicationOptimisticLockException();
        }

        foundAccount.setName(modifiedAccount.getName());
        foundAccount.setLastname(modifiedAccount.getLastname());
        foundAccount.setPhoneNumber(modifiedAccount.getPhoneNumber());
        foundAccount.setTwoFactorAuth(modifiedAccount.getTwoFactorAuth());

        for (UserLevel foundUserLevel : foundAccount.getUserLevels()) {
            UserLevel modifiedUserLevel = modifiedAccount.getUserLevels().stream()
                    .filter((level) -> level.getClass().getSimpleName().equalsIgnoreCase(foundUserLevel.getClass().getSimpleName()))
                    .findFirst().orElseThrow(UserLevelMissingException::new);

            if (!foundUserLevel.getVersion().equals(modifiedUserLevel.getVersion())) {
                throw new ApplicationOptimisticLockException();
            }
        }

        accountFacade.edit(foundAccount);
        historyDataFacade.create(new AccountHistoryData(foundAccount,
                OperationType.PERSONAL_DATA_MODIFICATION,
                accountFacade.findByLogin(SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName())
                        .orElse(null)));

        return foundAccount;
    }

    // Activate account method

    @Override
    @RolesAllowed({Roles.ANONYMOUS})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    public boolean activateAccount(String token) throws ApplicationBaseException {
        String decodedTokenValue = new String(Base64.getUrlDecoder().decode(token.getBytes()));
        Token tokenFromDB = tokenFacade.findByTokenValue(decodedTokenValue).orElseThrow(() -> new TokenNotFoundException(I18n.TOKEN_VALUE_NOT_FOUND_EXCEPTION));
        Account account = accountFacade.find(jwtProvider.extractAccountId(tokenFromDB.getTokenValue()))
                .orElseThrow(AccountIdNotFoundException::new);
        if (jwtProvider.isTokenValid(tokenFromDB.getTokenValue(), account)) {
            account.setActive(true);

            accountFacade.edit(account);
            historyDataFacade.create(new AccountHistoryData(account,
                    OperationType.ACTIVATION,
                    accountFacade.findByLogin(SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getName()).orElse(null)));

            tokenFacade.remove(tokenFromDB);
            mailProvider.sendActivationConfirmationEmail(account.getName(),
                    account.getLastname(),
                    account.getEmail(),
                    account.getAccountLanguage());
            return true;
        }
        return false;
    }

    // E-mail change methods

    @Override
    @RolesAllowed({Roles.ANONYMOUS})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    public boolean confirmEmail(String token) throws ApplicationBaseException {
        String decodedTokenValue = new String(Base64.getUrlDecoder().decode(token.getBytes()));
        Token tokenFromDB = tokenFacade.findByTokenValue(decodedTokenValue).orElseThrow(() -> new TokenNotFoundException(I18n.TOKEN_NOT_FOUND_EXCEPTION));

        Optional<Account> accountFromDB = accountFacade.find(jwtProvider.extractAccountId(decodedTokenValue));
        Account account = accountFromDB.orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (jwtProvider.isTokenValid(decodedTokenValue, account)) {
            String email = jwtProvider.extractEmail(decodedTokenValue);
            if (email == null) throw new AccountEmailNullException(I18n.ACCOUNT_EMAIL_FROM_TOKEN_NULL_EXCEPTION);
            account.setEmail(email);

            accountFacade.edit(account);
            historyDataFacade.create(new AccountHistoryData(account,
                    OperationType.EMAIL_CHANGE,
                    accountFacade.findByLogin(SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getName())
                            .orElse(null)));

            tokenFacade.remove(tokenFromDB);
            return true;
        }
        return false;
    }

    /**
     * Changes the e-mail of the specified Account.
     *
     * @param accountId ID of the account which the e-mail will be changed.
     * @param newEmail  New e-mail address.
     * @throws ApplicationBaseException General superclass for all exceptions thrown by exception handling aspects in facade layer.
     */
    @Override
    @RolesAllowed({Roles.AUTHENTICATED})
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeEmail(UUID accountId, String newEmail) throws ApplicationBaseException {
        Account account = accountFacade.find(accountId).orElseThrow(AccountNotFoundException::new);
        if (Objects.equals(account.getEmail(), newEmail))
            throw new AccountSameEmailException();
        if (accountFacade.findByEmail(newEmail).isPresent())
            throw new AccountEmailAlreadyTakenException();

        tokenFacade.removeByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL, account.getId());

        Token emailChangeToken = tokenProvider.generateEmailChangeToken(account, newEmail);
        this.tokenFacade.create(emailChangeToken);

        String encodedTokenValue = new String(Base64.getUrlEncoder().encode(emailChangeToken.getTokenValue().getBytes()));
        String confirmationURL = accountConfirmEmail + encodedTokenValue;

        mailProvider.sendEmailConfirmEmail(account.getName(), account.getLastname(), newEmail, confirmationURL, account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Roles.AUTHENTICATED})
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = TokenBaseException.class)
    public void resendEmailConfirmation() throws ApplicationBaseException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = accountFacade.findByLogin(login).orElseThrow(AccountNotFoundException::new);
        Token dbToken = tokenFacade.findByTypeAndAccount(Token.TokenType.CONFIRM_EMAIL, account.getId()).orElseThrow(TokenNotFoundException::new);

        tokenFacade.remove(dbToken);

        if (!jwtProvider.isTokenValid(dbToken.getTokenValue(), account)) {
            throw new TokenNotValidException();
        }

        String newEmail = jwtProvider.extractEmail(dbToken.getTokenValue());
        Token emailChangeToken = tokenProvider.generateEmailChangeToken(account, newEmail);

        String encodedTokenValue = new String(Base64.getUrlEncoder().encode(emailChangeToken.getTokenValue().getBytes()));
        String confirmationURL = accountConfirmEmail + encodedTokenValue;

        mailProvider.sendEmailConfirmEmail(account.getName(), account.getLastname(), newEmail, confirmationURL, account.getAccountLanguage());

        tokenFacade.create(emailChangeToken);
    }

    // Read methods

    @Override
    @RolesAllowed({Roles.ADMIN})
    public List<Account> getAccountsByMatchingLoginFirstNameAndLastName(String login,
                                                                        String firstName,
                                                                        String lastName,
                                                                        boolean active,
                                                                        boolean order,
                                                                        int pageNumber,
                                                                        int pageSize) throws ApplicationBaseException {
        return accountFacade.findAllAccountsByActiveAndLoginAndUserFirstNameAndUserLastNameWithPagination(
                login, firstName, lastName, active, order, pageNumber, pageSize);
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    public List<Account> getAllAccounts(int pageNumber, int pageSize) throws ApplicationBaseException {
        return accountFacade.findAllAccountsWithPagination(pageNumber, pageSize);
    }

    @Override
    @RolesAllowed({Roles.AUTHENTICATED})
    @Transactional(propagation = Propagation.REQUIRED)
    public Account getAccountByLogin(String login) throws ApplicationBaseException {
        return accountFacade.findByLogin(login).orElseThrow(AccountNotFoundException::new);
    }

    @RolesAllowed({Roles.ADMIN})
    @Override
    public Account getAccountById(UUID id) throws ApplicationBaseException {
        return accountFacade.findAndRefresh(id).orElseThrow(AccountNotFoundException::new);
    }

    // Add user level methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    public void addClientUserLevel(String id) throws ApplicationBaseException {

        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (account.getUserLevels().stream().anyMatch(userLevel -> userLevel instanceof Client)) {
            throw new AccountUserLevelException(I18n.USER_LEVEL_DUPLICATED);
        }

        if (account.getUserLevels().stream().anyMatch(userLevel -> userLevel instanceof Staff)) {
            throw new AccountUserLevelException(I18n.ACCOUNT_USER_LEVELS_CONFLICT);
        }

        UserLevel clientUserLevel = new Client();
        account.addUserLevel(clientUserLevel);
        userLevelFacade.create(clientUserLevel);
        accountFacade.edit(account);
        //TODO hmmmm?

        mailProvider.sendEmailNotificationAboutGrantedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.CLIENT_USER_LEVEL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    public void addStaffUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (account.getUserLevels().stream().anyMatch(userLevel -> userLevel instanceof Staff)) {
            throw new AccountUserLevelException(I18n.USER_LEVEL_DUPLICATED);
        }

        if (account.getUserLevels().stream().anyMatch(userLevel -> userLevel instanceof Client)) {
            throw new AccountUserLevelException(I18n.ACCOUNT_USER_LEVELS_CONFLICT);
        }

        UserLevel staffUserLevel = new Staff();
        account.addUserLevel(staffUserLevel);
        userLevelFacade.create(staffUserLevel);
        accountFacade.edit(account);
        //TODO hmmmm?

        mailProvider.sendEmailNotificationAboutGrantedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.STAFF_USER_LEVEL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
    public void addAdminUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new pl.lodz.p.it.ssbd2024.ssbd03.exceptions.account.read.AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (account.getUserLevels().stream().anyMatch(userLevel -> userLevel instanceof Admin)) {
            throw new AccountUserLevelException(I18n.USER_LEVEL_DUPLICATED);
        }

        UserLevel adminUserLevel = new Admin();
        account.addUserLevel(adminUserLevel);
        userLevelFacade.create(adminUserLevel);
        accountFacade.edit(account);
        //TODO hmmmm?

        mailProvider.sendEmailNotificationAboutGrantedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.ADMIN_USER_LEVEL,
                account.getAccountLanguage());
    }

    // Remove user level methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeClientUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (account.getUserLevels().stream().noneMatch(userLevel -> userLevel instanceof Client)) {
            throw new AccountUserLevelException(I18n.NO_SUCH_USER_LEVEL_EXCEPTION);
        }

        if (account.getUserLevels().size() == 1) {
            throw new AccountUserLevelException(I18n.ONE_USER_LEVEL);
        }

        UserLevel clientUserLevel = account.getUserLevels().stream().filter(userLevel -> userLevel instanceof Client).findFirst().orElse(null);

        account.removeUserLevel(clientUserLevel);
        accountFacade.edit(account);
        //TODO hmmmm?

        userLevelFacade.remove(clientUserLevel);

        mailProvider.sendEmailNotificationAboutRevokedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.CLIENT_USER_LEVEL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeStaffUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (account.getUserLevels().stream().noneMatch(userLevel -> userLevel instanceof Staff)) {
            throw new AccountUserLevelException(I18n.NO_SUCH_USER_LEVEL_EXCEPTION);
        }

        if (account.getUserLevels().size() == 1) {
            throw new AccountUserLevelException(I18n.ONE_USER_LEVEL);
        }

        UserLevel staffUserLevel = account.getUserLevels().stream().filter(userLevel -> userLevel instanceof Staff).findFirst().orElse(null);

        account.removeUserLevel(staffUserLevel);
        accountFacade.edit(account);
        //TODO hmmmm?

        userLevelFacade.remove(staffUserLevel);

        mailProvider.sendEmailNotificationAboutRevokedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.STAFF_USER_LEVEL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Roles.ADMIN})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeAdminUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        String currentAccountLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        Account currentAccount = accountFacade.findByLogin(currentAccountLogin)
                .orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (account.equals(currentAccount)) {
            throw new AccountUserLevelException(I18n.ADMIN_ACCOUNT_REMOVE_OWN_ADMIN_USER_LEVEL_EXCEPTION);
        }

        if (account.getUserLevels().stream().noneMatch(userLevel -> userLevel instanceof Admin)) {
            throw new AccountUserLevelException(I18n.NO_SUCH_USER_LEVEL_EXCEPTION);
        }

        if (account.getUserLevels().size() == 1) {
            throw new AccountUserLevelException(I18n.ONE_USER_LEVEL);
        }

        UserLevel adminUserLevel = account.getUserLevels().stream().filter(userLevel -> userLevel instanceof Admin).findFirst().orElse(null);

        account.removeUserLevel(adminUserLevel);
        accountFacade.edit(account);
        //TODO hmmmm?

        userLevelFacade.remove(adminUserLevel);

        mailProvider.sendEmailNotificationAboutRevokedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.ADMIN_USER_LEVEL,
                account.getAccountLanguage());
    }

    // Restore access to user account methods


    @Override
    @RolesAllowed({Roles.ANONYMOUS})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateAccessRestoreTokenAndSendEmailMessage(String email) throws ApplicationBaseException {
        Account account = accountFacade.findByEmail(email).orElseThrow(AccountEmailNotFoundException::new);
        tokenFacade.removeByTypeAndAccount(Token.TokenType.RESTORE_ACCESS_TOKEN, account.getId());

        if (account.couldAuthenticate()) {
            throw new AccountRestoreAccessException();
        }

        Token restoreTokenObject = tokenProvider.generateRestoreAccessToken(account);
        tokenFacade.create(restoreTokenObject);

        String encodedTokenValue = new String(Base64.getUrlEncoder().encode(restoreTokenObject.getTokenValue().getBytes()));
        String accessRestoreURL = this.restoreAccountAccessURL + encodedTokenValue;

        mailProvider.sendAccountAccessRestoreEmailMessage(account.getName(),
                account.getLastname(),
                account.getEmail(),
                accessRestoreURL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Roles.ANONYMOUS})
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void restoreAccountAccess(String token) throws ApplicationBaseException {
        Token tokenObject = tokenFacade.findByTokenValue(new String(Base64.getUrlDecoder().decode(token))).orElseThrow(TokenNotFoundException::new);
        Account account = accountFacade.find(tokenObject.getAccount().getId()).orElseThrow(AccountIdNotFoundException::new);

        tokenFacade.remove(tokenObject);

        if (account.couldAuthenticate()) {
            throw new AccountRestoreAccessException();
        }

        if (!jwtProvider.isTokenValid(tokenObject.getTokenValue(), account)) {
            throw new TokenNotValidException();
        }

        account.setSuspended(false);

        accountFacade.edit(account);
        historyDataFacade.create(new AccountHistoryData(account,
                OperationType.RESTORE_ACCESS,
                accountFacade.findByLogin(SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName())
                        .orElse(null)));

        mailProvider.sendAccountAccessRestoreInfoEmail(account.getName(),
                account.getLastname(),
                account.getEmail(),
                account.getAccountLanguage());
    }
}
