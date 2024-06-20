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
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.AccountRestoreAccessException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.AccountUserLevelException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.conflict.AccountAlreadyBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.conflict.AccountAlreadyUnblockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.conflict.AccountEmailAlreadyTakenException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.conflict.AccountSameEmailException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.integrity.UserLevelMissingException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read.AccountEmailNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read.AccountEmailNullException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read.AccountIdNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.resetOwnPassword.IncorrectPasswordException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.resetOwnPassword.PasswordPreviouslyUsedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.status.AccountBlockedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.token.read.TokenNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.token.TokenNotValidException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.attribute.AttributeNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.attribute.AttributeRepeatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountHistoryDataFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.UserLevelFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.attribute.AttributeNameFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.attribute.AttributeValueFacade;
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
@LoggerInterceptor
@TxTracked
@Transactional(propagation = Propagation.MANDATORY)
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
     * Facade component used to manage attributes names in the database.
     */
    private final AttributeNameFacade attributeNameFacade;
    /**
     * Facade component used to manage attributes values in the database.
     */
    private final AttributeValueFacade attributeValueFacade;

    /**
     * Autowired constructor for the service.
     *
     * @param accountFacade        Facade responsible for users accounts management.
     * @param historyDataFacade    Facade used for inserting information about account modifications to the database.
     * @param passwordEncoder      This component is used to generate hashed passwords for user accounts (not to store them in their original form).
     * @param tokenFacade          This facade is responsible for manipulating tokens, used for various, user account related operations.
     * @param mailProvider         This component is used to send e-mail messages to e-mail address of users (where message depends on their actions).
     * @param jwtProvider          This component is used to generate token values for token facade.
     * @param userLevelFacade      It is used to create new tokens, remove them, etc.
     * @param attributeNameFacade  Facade for handling attribute names.
     * @param attributeValueFacade Facade for handling attribute values.
     */
    @Autowired
    public AccountService(AccountMOKFacade accountFacade,
                          AccountHistoryDataFacade historyDataFacade,
                          PasswordEncoder passwordEncoder,
                          TokenFacade tokenFacade,
                          MailProvider mailProvider,
                          JWTProvider jwtProvider,
                          TokenProvider tokenProvider,
                          UserLevelFacade userLevelFacade,
                          AttributeNameFacade attributeNameFacade,
                          AttributeValueFacade attributeValueFacade) {
        this.accountFacade = accountFacade;
        this.historyDataFacade = historyDataFacade;
        this.passwordEncoder = passwordEncoder;
        this.tokenFacade = tokenFacade;
        this.mailProvider = mailProvider;
        this.jwtProvider = jwtProvider;
        this.tokenProvider = tokenProvider;
        this.userLevelFacade = userLevelFacade;
        this.attributeNameFacade = attributeNameFacade;
        this.attributeValueFacade = attributeValueFacade;
    }

    // Register user account methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Authorities.REGISTER_CLIENT, Authorities.REGISTER_USER})
    public Account registerClient(String login, String password, String firstName, String lastName, String email, String phoneNumber, String language)
            throws ApplicationBaseException {
        Account newClientAccount = new Account(login, passwordEncoder.encode(password), firstName, lastName, email, phoneNumber);
        newClientAccount.setAccountLanguage(language);
        newClientAccount.getPreviousPasswords().add(newClientAccount.getPassword());
        UserLevel clientUserLevel = new Client();
        clientUserLevel.setAccount(newClientAccount);
        newClientAccount.addUserLevel(clientUserLevel);

        accountFacade.create(newClientAccount);
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
    @RolesAllowed({Authorities.REGISTER_USER})
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
    @RolesAllowed({Authorities.REGISTER_USER})
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

    @Override
    @RolesAllowed({Authorities.RESET_PASSWORD})
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

    @Override
    @RolesAllowed({Authorities.RESET_PASSWORD, Authorities.CHANGE_OWN_PASSWORD_AFTER_ADMINISTRATIVE_CHANGE})
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

        String hashedPassword = passwordEncoder.encode(newPassword);
        account.getPreviousPasswords().add(hashedPassword);
        account.setPassword(hashedPassword);

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
    @RolesAllowed({Authorities.CHANGE_OWN_PASSWORD})
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
    @RolesAllowed({Authorities.BLOCK_ACCOUNT})
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
    @RolesAllowed({Authorities.UNBLOCK_ACCOUNT})
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
    @RolesAllowed({Authorities.MODIFY_OWN_ACCOUNT, Authorities.MODIFY_USER_ACCOUNT})
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
    @RolesAllowed({Authorities.CONFIRM_ACCOUNT_CREATION})
    public boolean activateAccount(String token) throws ApplicationBaseException {
        String decodedTokenValue = new String(Base64.getUrlDecoder().decode(token.getBytes()));
        Token tokenFromDB = tokenFacade.findByTokenValue(decodedTokenValue).orElseThrow(() -> new TokenNotFoundException(I18n.TOKEN_VALUE_NOT_FOUND_EXCEPTION));
        Account account = accountFacade.find(jwtProvider.extractAccountId(tokenFromDB.getTokenValue()))
                .orElseThrow(AccountIdNotFoundException::new);
        if (jwtProvider.isTokenValid(tokenFromDB.getTokenValue(), account)) {
            account.activateAccount(true);

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
    @RolesAllowed({Authorities.CONFIRM_EMAIL_CHANGE})
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
    @RolesAllowed({Authorities.CHANGE_OWN_MAIL, Authorities.CHANGE_USER_MAIL})
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
    @RolesAllowed({Authorities.RESEND_EMAIL_CONFIRMATION_MAIL})
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
    @RolesAllowed({Authorities.GET_ALL_USER_ACCOUNTS})
    public List<Account> getAccountsMatchingPhraseInNameOrLastname(String phrase,
                                                                   String orderBy,
                                                                   boolean order,
                                                                   int pageNumber,
                                                                   int pageSize) throws ApplicationBaseException {
        return accountFacade.findAccountsMatchingPhraseInNameOrLastnameWithPagination(
                phrase, orderBy, order, pageNumber, pageSize);
    }

    @Override
    @RolesAllowed({Authorities.GET_ALL_USER_ACCOUNTS})
    public List<Account> getAllAccounts(int pageNumber, int pageSize) throws ApplicationBaseException {
        return accountFacade.findAllAccountsWithPagination(pageNumber, pageSize);
    }

    @Override
    @RolesAllowed({Authorities.GET_OWN_HISTORICAL_DATA, Authorities.GET_OWN_ACCOUNT, Authorities.CHANGE_OWN_MAIL})
    public Account getAccountByLogin(String login) throws ApplicationBaseException {
        return accountFacade.findByLogin(login).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    @RolesAllowed({Authorities.GET_USER_ACCOUNT, Authorities.CHANGE_USER_PASSWORD, Authorities.BLOCK_ACCOUNT})
    public Account getAccountById(UUID id) throws ApplicationBaseException {
        return accountFacade.findAndRefresh(id).orElseThrow(AccountNotFoundException::new);
    }

    // Add user level methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Authorities.ADD_USER_LEVEL})
    public void addClientUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

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

        mailProvider.sendEmailNotificationAboutGrantedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.CLIENT_USER_LEVEL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Authorities.ADD_USER_LEVEL})
    public void addStaffUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

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

        mailProvider.sendEmailNotificationAboutGrantedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.STAFF_USER_LEVEL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Authorities.ADD_USER_LEVEL})
    public void addAdminUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (account.getUserLevels().stream().anyMatch(userLevel -> userLevel instanceof Admin)) {
            throw new AccountUserLevelException(I18n.USER_LEVEL_DUPLICATED);
        }

        UserLevel adminUserLevel = new Admin();
        account.addUserLevel(adminUserLevel);
        userLevelFacade.create(adminUserLevel);
        accountFacade.edit(account);

        mailProvider.sendEmailNotificationAboutGrantedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.ADMIN_USER_LEVEL,
                account.getAccountLanguage());
    }

    // Remove user level methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Authorities.REMOVE_USER_LEVEL})
    public void removeClientUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (account.getUserLevels().stream().noneMatch(userLevel -> userLevel instanceof Client)) {
            throw new AccountUserLevelException(I18n.NO_SUCH_USER_LEVEL_EXCEPTION);
        }

        if (account.getUserLevels().size() == 1) {
            throw new AccountUserLevelException(I18n.ONE_USER_LEVEL);
        }

        UserLevel clientUserLevel = account.getUserLevels()
                .stream()
                .filter(userLevel -> userLevel instanceof Client)
                .findFirst()
                .orElse(null);


        account.removeUserLevel(clientUserLevel);
        accountFacade.edit(account);

        userLevelFacade.remove(clientUserLevel);

        mailProvider.sendEmailNotificationAboutRevokedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.CLIENT_USER_LEVEL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Authorities.REMOVE_USER_LEVEL})
    public void removeStaffUserLevel(String id) throws ApplicationBaseException {
        Account account = accountFacade.find(UUID.fromString(id)).orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));

        if (account.getUserLevels().stream().noneMatch(userLevel -> userLevel instanceof Staff)) {
            throw new AccountUserLevelException(I18n.NO_SUCH_USER_LEVEL_EXCEPTION);
        }

        if (account.getUserLevels().size() == 1) {
            throw new AccountUserLevelException(I18n.ONE_USER_LEVEL);
        }

        UserLevel staffUserLevel = account.getUserLevels()
                .stream()
                .filter(userLevel -> userLevel instanceof Staff)
                .findFirst()
                .orElse(null);


        account.removeUserLevel(staffUserLevel);
        accountFacade.edit(account);

        userLevelFacade.remove(staffUserLevel);

        mailProvider.sendEmailNotificationAboutRevokedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.STAFF_USER_LEVEL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Authorities.REMOVE_USER_LEVEL})
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

        UserLevel adminUserLevel = account.getUserLevels()
                .stream()
                .filter(userLevel -> userLevel instanceof Admin)
                .findFirst()
                .orElse(null);


        account.removeUserLevel(adminUserLevel);
        accountFacade.edit(account);

        userLevelFacade.remove(adminUserLevel);

        mailProvider.sendEmailNotificationAboutRevokedUserLevel(account.getName(),
                account.getLastname(),
                account.getEmail(),
                I18n.ADMIN_USER_LEVEL,
                account.getAccountLanguage());
    }

    // Restore access to user account methods


    @Override
    @RolesAllowed({Authorities.RESTORE_ACCOUNT_ACCESS})
    public void generateAccessRestoreTokenAndSendEmailMessage(String email) throws ApplicationBaseException {
        Account account = accountFacade.findByEmail(email).orElseThrow(AccountEmailNotFoundException::new);
        tokenFacade.removeByTypeAndAccount(Token.TokenType.RESTORE_ACCESS_TOKEN, account.getId());

        if (account.isEnabled()) {
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
    @RolesAllowed({Authorities.RESTORE_ACCOUNT_ACCESS})
    public void restoreAccountAccess(String token) throws ApplicationBaseException {
        Token tokenObject = tokenFacade.findByTokenValue(new String(Base64.getUrlDecoder().decode(token))).orElseThrow(TokenNotFoundException::new);
        Account account = accountFacade.find(tokenObject.getAccount().getId()).orElseThrow(AccountIdNotFoundException::new);

        tokenFacade.remove(tokenObject);

        if (account.isEnabled()) {
            throw new AccountRestoreAccessException();
        }

        if (!jwtProvider.isTokenValid(tokenObject.getTokenValue(), account)) {
            throw new TokenNotValidException();
        }

        account.activateAccount(false);

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

    @RolesAllowed({Authorities.CHANGE_USER_PASSWORD})
    public void forgetAccountPasswordByAdmin(String userEmail) throws ApplicationBaseException {
        Account account = this.accountFacade.findByEmail(userEmail).orElseThrow(AccountEmailNotFoundException::new);

        if (account.getBlocked()) throw new AccountBlockedException();
        else if (!account.getActive()) throw new AccountNotActivatedException();

        tokenFacade.removeByTypeAndAccount(Token.TokenType.CHANGE_OVERWRITTEN_PASSWORD, account.getId());

        Token passwordResetToken = tokenProvider.generateAdminPasswordResetToken(account);
        this.tokenFacade.create(passwordResetToken);

        String encodedTokenValue = new String(Base64.getUrlEncoder().encode(passwordResetToken.getTokenValue().getBytes()));
        String passwordResetURL = this.accountPasswordResetUrl + encodedTokenValue;

        this.mailProvider.sendPasswordResetEmail(account.getName(),
                account.getLastname(),
                userEmail,
                passwordResetURL,
                account.getAccountLanguage());
    }

    @Override
    @RolesAllowed({Authorities.GET_ADMIN_PASSWORD_RESET_STATUS})
    public boolean getPasswordAdminResetStatus() throws ApplicationBaseException {
        Account account = accountFacade.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new AccountNotFoundException(I18n.ACCOUNT_NOT_FOUND_EXCEPTION));
        return tokenFacade.findByTypeAndAccount(Token.TokenType.CHANGE_OVERWRITTEN_PASSWORD, account.getId()).isPresent();
    }

    @Override
    @RolesAllowed({Authorities.GET_OWN_HISTORICAL_DATA, Authorities.GET_ACCOUNT_HISTORICAL_DATA})
    public List<AccountHistoryData> getHistoryDataByAccountId(UUID id, int pageNumber, int pageSize) throws ApplicationBaseException {
        return historyDataFacade.findByAccountId(id, pageNumber, pageSize);
    }

    @Override
    @RolesAllowed({Authorities.MANAGE_OWN_ATTRIBUTES, Authorities.MANAGE_ATTRIBUTES})
    public List<AttributeName> getAllAttributesNames(int pageNumber, int pageSize) throws ApplicationBaseException {
        return attributeNameFacade.findAllAttributeNamesWithPagination(pageNumber, pageSize);
    }

    @Override
    @RolesAllowed({Authorities.MANAGE_OWN_ATTRIBUTES, Authorities.MANAGE_ATTRIBUTES})
    public List<AttributeValue> getAllAttributeValues(String attributeName, int pageNumber, int pageSize) throws ApplicationBaseException {
        return attributeValueFacade.findByAttributeName(attributeName, pageNumber, pageSize);
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    public void addAttribute(String attributeName) throws ApplicationBaseException {
        attributeNameFacade.create(new AttributeName(attributeName));
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    public void removeAttribute(String attributeName) throws ApplicationBaseException {
        attributeNameFacade.removeByName(attributeName);
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    public void addAttributeValue(String attributeName, String attributeValue) throws ApplicationBaseException {
        AttributeName attributeNameObject = attributeNameFacade.findByName(attributeName).orElseThrow(AttributeNotFoundException::new);

        AttributeValue attributeValueObject = new AttributeValue();
        attributeValueObject.setAttributeValue(attributeValue);
        attributeValueObject.setAttributeNameId(attributeNameObject);

        attributeNameObject.getListOfAttributeValues().add(attributeValueObject);

        attributeValueFacade.create(attributeValueObject);
        attributeNameFacade.edit(attributeNameObject);
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    public void removeAttributeValue(String attributeName, String attributeValue) throws ApplicationBaseException {
        AttributeName attributeNameObject = attributeNameFacade.findByName(attributeName).orElseThrow(AttributeNotFoundException::new);

        AttributeValue attributeValueObject = attributeNameObject.getListOfAttributeValues()
                .stream()
                .filter(obj -> obj.getAttributeValue().equals(attributeValue))
                .findAny()
                .orElse(null);

        attributeNameObject.getListOfAttributeValues().remove(attributeValueObject);

        attributeValueFacade.remove(attributeValueObject);
        attributeNameFacade.edit(attributeNameObject);
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_OWN_ATTRIBUTES)
    public void assignAttribute(String userLogin, String attributeName, String attributeValue) throws ApplicationBaseException {
        Account account = accountFacade.findByLogin(userLogin).orElseThrow(AccountNotFoundException::new);

        if (account.getAttributeRecords()
                .stream()
                .anyMatch(attributeRecord -> attributeRecord.getAttributeName().getAttributeName().equals(attributeName))) {
            throw new AttributeRepeatedException();
        }

        AttributeName attributeNameObject = attributeNameFacade.findByName(attributeName).orElseThrow(AttributeNotFoundException::new);
        AttributeValue attributeValueObject = attributeNameObject.getListOfAttributeValues()
                .stream()
                .filter(obj -> obj.getAttributeValue().equals(attributeValue))
                .findAny()
                .orElseThrow(AttributeNotFoundException::new);

        AttributeRecord attributeRecord = new AttributeRecord();
        attributeRecord.setAttributeName(attributeNameObject);
        attributeRecord.setAttributeValue(attributeValueObject);

        account.getAttributeRecords().add(attributeRecord);

        accountFacade.edit(account);
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_OWN_ATTRIBUTES)
    public void removeAttribute(String userLogin, String attributeName) throws ApplicationBaseException {
        Account account = accountFacade.findByLogin(userLogin).orElseThrow(AccountNotFoundException::new);

        account.getAttributeRecords().removeIf(attributeRecord -> attributeRecord.getAttributeName().getAttributeName().equals(attributeName));

        accountFacade.edit(account);
    }
}
