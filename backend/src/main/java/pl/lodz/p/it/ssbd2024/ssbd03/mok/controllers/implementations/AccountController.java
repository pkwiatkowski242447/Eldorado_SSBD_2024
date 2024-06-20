package pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.implementations;

import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.RollbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.AttributeDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountChangePasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountEmailDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountModifyDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountInputDTO.AccountPasswordDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountHistoryDataOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountListDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO.AccountOutputDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.exception.ExceptionDTO;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok.AccountHistoryDataMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok.AccountListMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok.AccountMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.mappers.mok.AttributeMapper;
import pl.lodz.p.it.ssbd2024.ssbd03.config.security.consts.Authorities;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AttributeValue;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.AttributeName;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationDatabaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationOptimisticLockException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.integrity.AccountDataIntegrityCompromisedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.read.AccountNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.account.status.AccountNotActivatedException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.request.InvalidRequestHeaderIfMatchException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.mok.token.TokenBaseException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.IllegalOperationException;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils.InvalidDataFormatException;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.controllers.interfaces.AccountControllerInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces.AccountServiceInterface;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.I18n;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.providers.JWTProvider;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller used for manipulating user accounts in the system.
 */
@Slf4j
@RestController
@LoggerInterceptor
@RequestMapping(value = "/api/v1/accounts")
@TxTracked
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ApplicationBaseException.class)
public class AccountController implements AccountControllerInterface {

    /**
     * AccountServiceInterface used for operation on accounts.
     */
    private final AccountServiceInterface accountService;

    /**
     * JWTProvider used for operations on JWT TOKEN.
     */
    private final JWTProvider jwtProvider;

    /**
     * Autowired constructor for the controller.
     * It is basically used to perform dependency injection of AccountService into this controller.
     *
     * @param accountService Service containing various methods for account manipulation.
     * @param jwtProvider    Service used for JWT management (eg. signing).
     */
    @Autowired
    public AccountController(AccountServiceInterface accountService,
                             JWTProvider jwtProvider) {
        this.accountService = accountService;
        this.jwtProvider = jwtProvider;
    }

    // Block & unblock account methods

    @Override
    @RolesAllowed({Authorities.BLOCK_ACCOUNT})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> blockAccount(String id) throws ApplicationBaseException {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null &&
                    SecurityContextHolder.getContext().getAuthentication().getName()
                            .equals(accountService.getAccountById(UUID.fromString(id)).getLogin())) {
                throw new IllegalOperationException(I18n.ACCOUNT_TRY_TO_BLOCK_OWN_EXCEPTION);
            }

            accountService.blockAccount(UUID.fromString(id));
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }

        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.UNBLOCK_ACCOUNT})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> unblockAccount(String id) throws ApplicationBaseException {
        try {
            accountService.unblockAccount(UUID.fromString(id));
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
        return ResponseEntity.noContent().build();
    }

    // Password change methods

    @Override
    @RolesAllowed({Authorities.RESET_PASSWORD})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> forgetAccountPassword(AccountEmailDTO accountEmailDTO) throws ApplicationBaseException {
        try {
            accountService.forgetAccountPassword(accountEmailDTO.getEmail());
        } catch (AccountNotFoundException | AccountNotActivatedException exception) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.CHANGE_USER_PASSWORD})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> resetAccountPassword(String id) throws ApplicationBaseException {
        try {
            UUID uuid = UUID.fromString(id);
            Account account = accountService.getAccountById(uuid);
            this.accountService.forgetAccountPasswordByAdmin(account.getEmail());

            return ResponseEntity.noContent().build();
        } catch (AccountNotFoundException accountNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ExceptionDTO(accountNotFoundException.getMessage()));
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InvalidDataFormatException(I18n.BAD_UUID_INVALID_FORMAT_EXCEPTION);
        }
    }

    @Override
    @RolesAllowed({Authorities.RESET_PASSWORD, Authorities.CHANGE_OWN_PASSWORD_AFTER_ADMINISTRATIVE_CHANGE})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> changeAccountPassword(String token, AccountPasswordDTO accountPasswordDTO)
            throws ApplicationBaseException {
        this.accountService.changeAccountPassword(token, accountPasswordDTO.getPassword());
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.CHANGE_OWN_PASSWORD})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> changePasswordSelf(AccountChangePasswordDTO accountChangePasswordDTO) throws ApplicationBaseException {
        String oldPassword = accountChangePasswordDTO.getOldPassword();
        String newPassword = accountChangePasswordDTO.getNewPassword();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        accountService.changePasswordSelf(oldPassword, newPassword, username);

        return ResponseEntity.ok().build();
    }

    // Read methods

    @Override
    @RolesAllowed(Authorities.GET_ALL_USER_ACCOUNTS)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    public ResponseEntity<?> getAllUsers(int pageNumber, int pageSize) throws ApplicationBaseException {
        List<AccountListDTO> accountList = accountService.getAllAccounts(pageNumber, pageSize)
                .stream()
                .map(AccountListMapper::toAccountListDTO)
                .toList();
        if (accountList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(accountList);
    }

    @Override
    @RolesAllowed({Authorities.GET_ALL_USER_ACCOUNTS})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    public ResponseEntity<?> getAccountsMatchingPhraseInNameOrLastname(String phrase, String orderBy, boolean order,
                                                                       int pageNumber, int pageSize)
            throws ApplicationBaseException {
        List<AccountListDTO> accountList = accountService.getAccountsMatchingPhraseInNameOrLastname(
                        phrase, orderBy, order, pageNumber, pageSize)
                .stream()
                .map(AccountListMapper::toAccountListDTO)
                .toList();
        if (accountList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(accountList);
    }

    @Override
    @RolesAllowed({Authorities.GET_OWN_ACCOUNT})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    public ResponseEntity<?> getSelf() throws ApplicationBaseException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.getAccountByLogin(login);

        AccountOutputDTO accountDTO = AccountMapper.toAccountOutputDto(account);
        HttpHeaders headers = new HttpHeaders();
        headers.setETag(String.format("\"%s\"", jwtProvider.generateObjectSignature(accountDTO)));

        return ResponseEntity.ok().headers(headers).body(accountDTO);
    }

    @Override
    @RolesAllowed({Authorities.GET_USER_ACCOUNT})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class})
    public ResponseEntity<?> getUserById(String id) throws ApplicationBaseException {
        try {
            UUID uuid = UUID.fromString(id);
            Account account = accountService.getAccountById(uuid);

            AccountOutputDTO accountOutputDTO = AccountMapper.toAccountOutputDto(account);
            HttpHeaders headers = new HttpHeaders();
            headers.setETag(String.format("\"%s\"", jwtProvider.generateObjectSignature(accountOutputDTO)));

            return ResponseEntity.ok().headers(headers).body(accountOutputDTO);
        } catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.badRequest().body(I18n.UUID_INVALID);
        } catch (AccountNotFoundException accountNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ExceptionDTO(accountNotFoundException.getMessage()));
        }
    }

    // Activate account method

    @Override
    @RolesAllowed({Authorities.CONFIRM_ACCOUNT_CREATION})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> activateAccount(String token) throws ApplicationBaseException {
        if (accountService.activateAccount(token)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(I18n.ACCOUNT_ACTIVATION_TIMED_OUT_EXCEPTION);
        }
    }

    // E-mail change methods

    @Override
    @RolesAllowed({Authorities.CHANGE_OWN_MAIL})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> changeEmailSelf(AccountEmailDTO accountEmailDTO) throws ApplicationBaseException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Account user = accountService.getAccountByLogin(login);
        accountService.changeEmail(user.getId(), accountEmailDTO.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.CHANGE_USER_MAIL})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> changeEmail(UUID id, AccountEmailDTO accountEmailDTO) throws ApplicationBaseException {
        accountService.changeEmail(id, accountEmailDTO.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.CONFIRM_EMAIL_CHANGE})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> confirmEmail(String token) throws ApplicationBaseException {
        if (accountService.confirmEmail(token)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().body(I18n.TOKEN_INVALID_OR_EXPIRED);
        }
    }

    @Override
    @RolesAllowed({Authorities.RESEND_EMAIL_CONFIRMATION_MAIL})
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {TokenBaseException.class})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> resendEmailConfirmation() throws ApplicationBaseException {
        accountService.resendEmailConfirmation();
        return ResponseEntity.noContent().build();
    }

    // Modify account methods

    @Override
    @RolesAllowed({Authorities.MODIFY_OWN_ACCOUNT})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> modifyAccountSelf(String ifMatch, AccountModifyDTO accountModifyDTO)
            throws ApplicationBaseException {
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new InvalidRequestHeaderIfMatchException();
        }

        if (!ifMatch.equals(jwtProvider.generateObjectSignature(accountModifyDTO))) {
            throw new AccountDataIntegrityCompromisedException();
        }

        //TODO maybe handle null (other methods same)??
        String currentUserLogin = SecurityContextHolder.getContext().getAuthentication().getName();

        AccountOutputDTO accountOutputDTO = AccountMapper.toAccountOutputDto(
                accountService.modifyAccount(AccountMapper.toAccount(accountModifyDTO), currentUserLogin)
        );
        return ResponseEntity.ok().body(accountOutputDTO);
    }

    @Override
    @RolesAllowed({Authorities.MODIFY_USER_ACCOUNT})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> modifyUserAccount(String ifMatch, AccountModifyDTO accountModifyDTO)
            throws ApplicationBaseException {
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new InvalidRequestHeaderIfMatchException();
        }

        if (!ifMatch.equals(jwtProvider.generateObjectSignature(accountModifyDTO))) {
            throw new AccountDataIntegrityCompromisedException();
        }

        AccountOutputDTO accountOutputDTO = AccountMapper.toAccountOutputDto(
                accountService.modifyAccount(AccountMapper.toAccount(accountModifyDTO), accountModifyDTO.getLogin())
        );
        return ResponseEntity.ok().body(accountOutputDTO);
    }

    // Add user level methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Authorities.ADD_USER_LEVEL})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> addClientUserLevel(String id) throws ApplicationBaseException {
        try {
            accountService.addClientUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.ADD_USER_LEVEL})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> addStaffUserLevel(String id) throws ApplicationBaseException {
        try {
            accountService.addStaffUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.ADD_USER_LEVEL})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> addAdminUserLevel(String id) throws ApplicationBaseException {
        try {
            accountService.addAdminUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

    // Remove user level methods - Client, Staff, Admin

    @Override
    @RolesAllowed({Authorities.REMOVE_USER_LEVEL})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> removeClientUserLevel(String id) throws ApplicationBaseException {
        try {
            accountService.removeClientUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.REMOVE_USER_LEVEL})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> removeStaffUserLevel(String id) throws ApplicationBaseException {
        try {
            accountService.removeStaffUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.REMOVE_USER_LEVEL})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> removeAdminUserLevel(String id) throws ApplicationBaseException {
        try {
            accountService.removeAdminUserLevel(String.valueOf(UUID.fromString(id)));
        } catch (IllegalArgumentException exception) {
            throw new InvalidDataFormatException();
        }
        return ResponseEntity.noContent().build();
    }

    // Restore access to user account methods

    @Override
    @RolesAllowed({Authorities.RESTORE_ACCOUNT_ACCESS})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> sendAccountRestorationEmailMessage(AccountEmailDTO accountEmailDTO) throws ApplicationBaseException {
        accountService.generateAccessRestoreTokenAndSendEmailMessage(accountEmailDTO.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.RESTORE_ACCOUNT_ACCESS})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> restoreAccountAccess(String tokenValue) throws ApplicationBaseException {
        accountService.restoreAccountAccess(tokenValue);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed({Authorities.GET_ADMIN_PASSWORD_RESET_STATUS})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"),
            retryFor = {ApplicationDatabaseException.class, RollbackException.class, ApplicationOptimisticLockException.class})
    public ResponseEntity<?> getPasswordAdminResetStatus() throws ApplicationBaseException {
        return ResponseEntity.ok().body(accountService.getPasswordAdminResetStatus());
    }

    @Override
    @RolesAllowed({Authorities.GET_OWN_HISTORICAL_DATA})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> getHistoryDataSelf(int pageNumber, int pageSize) throws ApplicationBaseException {
        Account account = accountService.getAccountByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        List<AccountHistoryDataOutputDTO> accountList = accountService.getHistoryDataByAccountId(account.getId(),pageNumber, pageSize)
                .stream()
                .map(AccountHistoryDataMapper::toAccountHistoryDataOutputDto)
                .toList();
        if (accountList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(accountList);
    }

    @Override
    @RolesAllowed({Authorities.GET_ACCOUNT_HISTORICAL_DATA})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> getHistoryDataByAccountId(String id, int pageNumber, int pageSize) throws ApplicationBaseException {
        List<AccountHistoryDataOutputDTO> accountList = accountService.getHistoryDataByAccountId(UUID.fromString(id), pageNumber, pageSize)
                .stream()
                .map(AccountHistoryDataMapper::toAccountHistoryDataOutputDto)
                .toList();
        if (accountList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(accountList);
    }

    @Override
    @RolesAllowed({Authorities.MANAGE_OWN_ATTRIBUTES, Authorities.MANAGE_ATTRIBUTES})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> getAllAttributesNames(int pageNumber, int pageSize)
            throws ApplicationBaseException {
        List<String> accountList = accountService.getAllAttributesNames(pageNumber, pageSize)
                .stream()
                .map(AttributeName::getAttributeName)
                .toList();
        if (accountList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(accountList);
    }

    @Override
    @RolesAllowed({Authorities.MANAGE_OWN_ATTRIBUTES, Authorities.MANAGE_ATTRIBUTES})
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> getAllAttributeValues(String attributeName, int pageNumber, int pageSize)
            throws ApplicationBaseException {
        List<String> accountList = accountService.getAllAttributeValues(attributeName, pageNumber, pageSize)
                .stream()
                .map(AttributeValue::getAttributeValue)
                .toList();
        if (accountList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(accountList);
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> addAttribute(String attributeName)
            throws ApplicationBaseException {
        accountService.addAttribute(attributeName);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> removeAttribute(String attributeName)
            throws ApplicationBaseException {
        accountService.removeAttribute(attributeName);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> addAttributeValue(String attributeName, String attributeValue)
            throws ApplicationBaseException {
        accountService.addAttributeValue(attributeName, attributeValue);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_ATTRIBUTES)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> removeAttributeValue(String attributeName, String attributeValue)
            throws ApplicationBaseException {
        accountService.removeAttributeValue(attributeName, attributeValue);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_OWN_ATTRIBUTES)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> getAllAccountAttributes() throws ApplicationBaseException {
        Account account = accountService.getAccountByLogin(SecurityContextHolder.getContext().getAuthentication().getName());

        List<AttributeDTO> attributeDTOList = account.getAttributeRecords()
                .stream()
                .map(AttributeMapper::toAttributeDTO)
                .collect(Collectors.toList());

        if (attributeDTOList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(attributeDTOList);
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_OWN_ATTRIBUTES)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> assignAttribute(String attributeName, String attributeValue)
            throws ApplicationBaseException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.assignAttribute(login, attributeName, attributeValue);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(Authorities.MANAGE_OWN_ATTRIBUTES)
    @Retryable(maxAttemptsExpression = "${retry.max.attempts}", backoff = @Backoff(delayExpression = "${retry.max.delay}"))
    public ResponseEntity<?> removeAttributeValue(String attributeName) throws ApplicationBaseException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.removeAttribute(login, attributeName);
        return ResponseEntity.noContent().build();
    }
}
