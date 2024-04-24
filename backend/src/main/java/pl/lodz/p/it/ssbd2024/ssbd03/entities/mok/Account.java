package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountsConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Entity representing a user account in the system.
 *
 * @see UserLevel
 * @see Admin
 * @see Client
 * @see Staff
 * @see ActivityLog
 */
@Entity
@Table(name = DatabaseConsts.ACCOUNT_TABLE)
@SecondaryTable(name = DatabaseConsts.PERSONAL_DATA_TABLE)
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@NamedQueries({
        // General queries
        @NamedQuery(
                name = "Account.findByLogin",
                query = """
                        SELECT a FROM Account a
                        WHERE a.login = :login
                        """
        ),

        @NamedQuery(
                name = "Account.findAccountByEmail",
                query = """
                        SELECT a FROM Account a
                        WHERE a.email = :email
                        """
        ),

        @NamedQuery(
                name = "Account.findAllAccounts",
                query = """
                        SELECT a FROM Account a
                        ORDER BY a.login
                        """
        ),

        @NamedQuery(
                name = "Account.findAllAccountsByActive",
                query = """
                        SELECT a FROM Account a
                        WHERE a.active = :active
                        ORDER BY a.login
                        """
        ),

        @NamedQuery(
                name = "Account.findAllAccountsMatchingGivenLogin",
                query = """
                        SELECT a FROM Account a
                        WHERE LOWER(a.login) LIKE CONCAT('%', LOWER(:login) , '%')
                        AND a.active = :active
                        ORDER BY a.login
                        """
        ),

        // Find accounts by user level
        @NamedQuery(
                name = "Account.findAccountsByUserLevelAndActive",
                query = """
                        SELECT DISTINCT a FROM Account a
                        JOIN a.userLevels ul
                        WHERE TYPE(ul) = :userLevel
                        ORDER BY a.login
                        """
        ),

        // Find all accounts for deletion
        @NamedQuery(
                name = "Account.findAllAccountsMarkedForDeletion",
                query = """
                        SELECT a FROM Account a
                        WHERE a.verified = false AND a.creationDate < :timestamp
                        ORDER BY a.login
                        """
        ),

        // Finding blocked accounts
        @NamedQuery(
                name = "Account.findAllAccountsByBlockedInAscOrder",
                query = """
                        SELECT a FROM Account a
                        WHERE a.blocked = :blocked
                        ORDER BY a.login ASC
                        """
        ),

        @NamedQuery(
                name = "Account.findAllBlockedAccountsThatWereBlockedByAdmin",
                query = """
                        SELECT a FROM Account a
                        WHERE a.blocked = true AND a.blockedTime is null
                        ORDER BY a.login ASC
                        """
        ),

        @NamedQuery(
                name = "Account.findAllBlockedAccountsThatWereBlockedByLoginIncorrectlyCertainAmountOfTimes",
                query = """
                        SELECT a FROM Account a
                        WHERE a.blocked = true AND a.blockedTime is not null
                        ORDER BY a.login ASC
                        """
        ),

        // Accounts that are active and yet email is still not verified
        @NamedQuery(
                name = "Account.findAllAccountsByVerifiedAndActiveInAscOrder",
                query = """
                        SELECT a FROM Account a
                        WHERE a.verified = :verified AND a.active = :active
                        ORDER BY a.login ASC
                        """
        ),

        // Find accounts matching user first name
        @NamedQuery(
                name = "Account.findAccountsByActiveAndMatchingUserFirstNameOrUserLastNameAndLoginInAscendingOrder",
                query = """
                        SELECT a FROM Account a
                        WHERE
                            (
                                LOWER(a.name) LIKE CONCAT('%', LOWER(:firstName), '%') OR
                                LOWER(a.lastname) LIKE CONCAT ('%', LOWER(:lastName), '%')
                            )
                            AND LOWER(a.login) LIKE CONCAT('%', LOWER(:login) , '%')
                        ORDER BY a.login ASC
                        """
        ),
        @NamedQuery(
                name = "Account.findAccountsByActiveAndMatchingUserFirstNameOrUserLastNameAndLoginInDescendingOrder",
                query = """
                        SELECT a FROM Account a
                        WHERE
                            a.active = :active AND
                            (
                                LOWER(a.name) LIKE CONCAT('%', LOWER(:firstName), '%') OR
                                LOWER(a.lastname) LIKE CONCAT ('%', LOWER(:lastName), '%')
                            )
                            AND LOWER(a.login) LIKE CONCAT('%', LOWER(:login) , '%')
                        ORDER BY a.login DESC
                        """
        ),

        // Connected to activity log
        @NamedQuery(
                name = "Account.findAccountsWithoutAnyActivityFrom",
                query = """
                        SELECT a FROM Account a
                        WHERE a.active = :active AND a.activityLog.lastSuccessfulLoginTime < :lastSuccessfulLoginTime
                        ORDER BY a.login ASC
                        """
        ),

        @NamedQuery(
                name = "Account.countAccountsWithoutAnyActivityFrom",
                query = """
                        SELECT COUNT(a) FROM Account a
                        WHERE a.active = :active AND a.activityLog.lastSuccessfulLoginTime < :lastSuccessfulLoginTime
                        """
        ),
})
public class Account extends AbstractEntity {

    @NotBlank(message = AccountMessages.LOGIN_BLANK)
    //@Pattern(regexp = AccountsConsts.LOGIN_REGEX, message = AccountMessages.LOGIN_REGEX_NOT_MET)
    @Size(min = AccountsConsts.LOGIN_MIN_LENGTH, message = AccountMessages.LOGIN_TOO_SHORT)
    @Size(max = AccountsConsts.LOGIN_MAX_LENGTH, message = AccountMessages.LOGIN_TOO_LONG)
    @Column(name = DatabaseConsts.ACCOUNT_LOGIN_COLUMN, unique = true, nullable = false, updatable = false, length = 32)
    private String login;

    @NotBlank(message = AccountMessages.PASSWORD_BLANK)
    @Size(min = AccountsConsts.PASSWORD_LENGTH, max = AccountsConsts.PASSWORD_LENGTH, message = AccountMessages.PASSWORD_INVALID_LENGTH)
    @Column(name = DatabaseConsts.ACCOUNT_PASSWORD_COLUMN, nullable = false, length = 60)
    @ToString.Exclude
    @Setter
    private String password;

    @NotNull(message = AccountMessages.VERIFIED_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_VERIFIED_COLUMN, nullable = false)
    @Setter
    private Boolean verified = false;

    @NotNull(message = AccountMessages.ACTIVE_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_ACTIVE_COLUMN, nullable = false)
    @Setter
    private Boolean active = false;

    @NotNull(message = AccountMessages.BLOCKED_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_BLOCKED_COLUMN, nullable = false)
    @Setter
    private Boolean blocked = false;

    @Column(name = DatabaseConsts.ACCOUNT_BLOCKED_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private LocalDateTime blockedTime;

    @NotBlank(message = AccountMessages.NAME_BLANK)
    //@Pattern(regexp = AccountsConsts.NAME_REGEX, message = AccountMessages.NAME_REGEX_NOT_MET)
    @Size(min = AccountsConsts.NAME_MIN_LENGTH, message = AccountMessages.NAME_TOO_SHORT)
    @Size(max = AccountsConsts.NAME_MAX_LENGTH, message = AccountMessages.NAME_TOO_LONG)
    @Column(name = DatabaseConsts.PERSONAL_DATA_NAME_COLUMN, table = DatabaseConsts.PERSONAL_DATA_TABLE, nullable = false, length = 32)
    @Setter
    private String name;

    @NotBlank(message = AccountMessages.LASTNAME_BLANK)
    //@Pattern(regexp = AccountsConsts.LASTNAME_REGEX, message = AccountMessages.LASTNAME_REGEX_NOT_MET)
    @Size(min = AccountsConsts.LASTNAME_MIN_LENGTH, message = AccountMessages.LASTNAME_TOO_SHORT)
    @Size(max = AccountsConsts.LASTNAME_MAX_LENGTH, message = AccountMessages.LASTNAME_TOO_LONG)
    @Column(name = DatabaseConsts.PERSONAL_DATA_LASTNAME_COLUMN, table = DatabaseConsts.PERSONAL_DATA_TABLE, nullable = false, length = 32)
    @Setter
    private String lastname;

    @Email(message = AccountMessages.EMAIL_NOT_MET)
    @Size(min = AccountsConsts.EMAIL_MIN_LENGTH, message = AccountMessages.EMAIL_TOO_SHORT)
    @Size(max = AccountsConsts.EMAIL_MAX_LENGTH, message = AccountMessages.EMAIL_TOO_LONG)
    @Column(name = DatabaseConsts.PERSONAL_DATA_EMAIL_COLUMN, table = DatabaseConsts.PERSONAL_DATA_TABLE, unique = true, nullable = false, length = 64)
    @Setter
    private String email;

    @Size(min = AccountsConsts.USER_LEVEL_MIN_SIZE, message = AccountMessages.USER_LEVEL_EMPTY)
    @Size(max = AccountsConsts.USER_LEVEL_MAX_SIZE, message = AccountMessages.USER_LEVEL_FULL)
    @OneToMany(mappedBy = DatabaseConsts.ACCOUNT_TABLE, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @ToString.Exclude
    private final Collection<UserLevel> userLevels = new ArrayList<>();

    @Embedded
    @Setter
    private ActivityLog activityLog = new ActivityLog();

    @NotBlank(message = AccountMessages.LANGUAGE_BLANK)
    //@Pattern(regexp = AccountsConsts.LANGUAGE_REGEX, message = AccountMessages.LANGUAGE_REGEX_NOT_MET)
    @Column(name = DatabaseConsts.ACCOUNT_LANGUAGE_COLUMN, nullable = false, length = 16)
    @Setter
    private String accountLanguage;

    @NotBlank(message = AccountMessages.PHONE_NUMBER_BLANK)
    //@Pattern(regexp = AccountsConsts.PHONE_NUMBER_REGEX, message = AccountMessages.PHONE_NUMBER_REGEX_NOT_MET)
    @Column(name = DatabaseConsts.ACCOUNT_PHONE_NUMBER_COLUMN, nullable = false, length = 32)
    @Getter
    @Setter
    private String phoneNumber;

    @Column(name = DatabaseConsts.ACCOUNT_CREATION_DATE_COLUMN, nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private LocalDateTime creationDate;

    /**
     * Constructs new Account entity.
     *
     * @param login       Account's login.
     * @param password    Account's password.
     * @param name        Account owner's firstname.
     * @param lastname    Account owner's lastname.
     * @param email       Email connected with the account.
     * @param phoneNumber Phone number connected with the account.
     */
    public Account(String login, String password, String name, String lastname, String email, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Adds new user level to the account.
     *
     * @param userLevel User level to be added to the account.
     */
    public void addUserLevel(UserLevel userLevel) {
        userLevels.add(userLevel);
    }

    /**
     * Removes user level from the account.
     *
     * @param userLevel User level to be removed from the account.
     */
    public void removeUserLevel(UserLevel userLevel) {
        userLevels.remove(userLevel);
    }

    /**
     * Retrieves ActivityLog connected with the Account. It ensures that an ActivityLog object exists, in case it doesn't a new ActivityLog is created.
     *
     * @return ActivityLog connected to the Account.
     */
    public ActivityLog getActivityLog() {
        return this.activityLog == null ? new ActivityLog() : this.activityLog;
    }

    /**
     * Blocks account and sets the time of applying the blockade to current time.
     */
    public void blockAccount() {
        this.blocked = true;
        this.blockedTime = LocalDateTime.now();
    }

    /**
     * Unblocks the account and resets the time of applying the blockade.
     */
    public void unblockAccount() {
        this.blocked = false;
        this.blockedTime = null;
    }
}
