package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountsConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountMessages;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a user account in the system. It stores information used to authenticate
 * like login, password, user personal data: first name, last name, email, phone number.
 * Besides that, account also contain list of available user levels and reference to activity log.
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
                        WHERE a.active = false AND a.creationTime < :timestamp
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
                        WHERE a.blocked = true AND a.blockedTime is not null AND a.blockedTime < :timestamp
                        ORDER BY a.login ASC
                        """
        ),

        // Find accounts matching user first name
        @NamedQuery(
                name = "Account.findAccountsByActiveAndMatchingUserFirstNameOrUserLastNameAndLoginInAscendingOrder",
                query = """
                        SELECT a FROM Account a
                        WHERE
                            a.active = :active AND
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
                        WHERE a.suspended = false
                            AND (
                                (a.activityLog.lastSuccessfulLoginTime IS NULL AND a.creationTime < :timestamp)
                                OR (a.activationTime < :timestamp AND a.activityLog.lastSuccessfulLoginTime < :timestamp)
                            )
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

    /**
     * User login, a unique identifier
     */
    @NotBlank(message = AccountMessages.LOGIN_BLANK)
    @Pattern(regexp = AccountsConsts.LOGIN_REGEX, message = AccountMessages.LOGIN_REGEX_NOT_MET)
    @Size(min = AccountsConsts.LOGIN_MIN_LENGTH, message = AccountMessages.LOGIN_TOO_SHORT)
    @Size(max = AccountsConsts.LOGIN_MAX_LENGTH, message = AccountMessages.LOGIN_TOO_LONG)
    @Column(name = DatabaseConsts.ACCOUNT_LOGIN_COLUMN, unique = true, nullable = false, updatable = false, length = 32)
    private String login;

    /**
     * User password, used for authentication.
     */
    @NotBlank(message = AccountMessages.PASSWORD_BLANK)
    @Size(min = AccountsConsts.PASSWORD_LENGTH, max = AccountsConsts.PASSWORD_LENGTH, message = AccountMessages.PASSWORD_INVALID_LENGTH)
    @Column(name = DatabaseConsts.ACCOUNT_PASSWORD_COLUMN, nullable = false, length = 60)
    @Setter
    private String password;

    /**
     * Set containing all past password hashes for given user account.
     */
    @Column(name = DatabaseConsts.ACCOUNT_PAST_PASSWORD_COLUMN)
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = DatabaseConsts.PAST_PASSWORD_TABLE,
            joinColumns = @JoinColumn(name = DatabaseConsts.PAST_PASSWORD_ACCOUNT_ID_COLUMN)
    )
    @Getter
    private final Set<String> previousPasswords = new HashSet<>();

    /**
     * Variable indicating whether the user account is suspended.
     */
    @NotNull(message = AccountMessages.SUSPENDED_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_SUSPENDED_COLUMN, nullable = false)
    @Setter
    private Boolean suspended = false;

    /**
     * Variable indicating whether the user account is active.
     */
    @NotNull(message = AccountMessages.ACTIVE_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_ACTIVE_COLUMN, nullable = false)
    @Setter
    private Boolean active = false;

    /**
     * Variable indicating whether the user account is blocked.
     */
    @NotNull(message = AccountMessages.BLOCKED_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_BLOCKED_COLUMN, nullable = false)
    @Setter
    private Boolean blocked = false;

    /**
     * Variable indicating whether user account has 2FA enabled.
     */
    @NotNull(message = AccountMessages.TWO_FACTOR_AUTH_NULL)
    @Column(name = DatabaseConsts.TWO_FACTOR_AUTH_COLUMN, nullable = false)
    @Setter
    private Boolean twoFactorAuth = true;

    /**
     * Time when the account was blocked.
     * In case this account is blocked by an admin, this time is not set.
     */
    @Column(name = DatabaseConsts.ACCOUNT_BLOCKED_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime blockedTime;

    /**
     * User's first name.
     */
    @NotBlank(message = AccountMessages.NAME_BLANK)
    @Pattern(regexp = AccountsConsts.NAME_REGEX, message = AccountMessages.NAME_REGEX_NOT_MET)
    @Size(min = AccountsConsts.NAME_MIN_LENGTH, message = AccountMessages.NAME_TOO_SHORT)
    @Size(max = AccountsConsts.NAME_MAX_LENGTH, message = AccountMessages.NAME_TOO_LONG)
    @Column(name = DatabaseConsts.PERSONAL_DATA_NAME_COLUMN, table = DatabaseConsts.PERSONAL_DATA_TABLE, nullable = false, length = 32)
    @Setter
    private String name;

    /**
     * User's last name
     */
    @NotBlank(message = AccountMessages.LASTNAME_BLANK)
    @Pattern(regexp = AccountsConsts.LASTNAME_REGEX, message = AccountMessages.LASTNAME_REGEX_NOT_MET)
    @Size(min = AccountsConsts.LASTNAME_MIN_LENGTH, message = AccountMessages.LASTNAME_TOO_SHORT)
    @Size(max = AccountsConsts.LASTNAME_MAX_LENGTH, message = AccountMessages.LASTNAME_TOO_LONG)
    @Column(name = DatabaseConsts.PERSONAL_DATA_LASTNAME_COLUMN, table = DatabaseConsts.PERSONAL_DATA_TABLE, nullable = false, length = 32)
    @Setter
    private String lastname;

    /**
     * User's email address
     */
    @Email(message = AccountMessages.EMAIL_CONSTRAINT_NOT_MET)
    @Size(min = AccountsConsts.EMAIL_MIN_LENGTH, message = AccountMessages.EMAIL_TOO_SHORT)
    @Size(max = AccountsConsts.EMAIL_MAX_LENGTH, message = AccountMessages.EMAIL_TOO_LONG)
    @Column(name = DatabaseConsts.PERSONAL_DATA_EMAIL_COLUMN, table = DatabaseConsts.PERSONAL_DATA_TABLE, unique = true, nullable = false, length = 64)
    @Setter
    private String email;

    /**
     * Collection of user levels associated with the account.
     */
    @Size(min = AccountsConsts.USER_LEVEL_MIN_SIZE, message = AccountMessages.USER_LEVEL_EMPTY)
    @Size(max = AccountsConsts.USER_LEVEL_MAX_SIZE, message = AccountMessages.USER_LEVEL_FULL)
    @OneToMany(mappedBy = DatabaseConsts.ACCOUNT_TABLE, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    //TODO: Rozwiązanie konfliktu z uprawnieniami przy wykonywaniu operacji Refresh (gdyż jest kaskada)
    private final Set<UserLevel> userLevels = new HashSet<>();

    /**
     * Embedded ActivityLog associated with the account.
     */
    @Embedded
    @Setter
    private ActivityLog activityLog = new ActivityLog();

    /**
     * Language preference for the account.
     */
    @NotBlank(message = AccountMessages.LANGUAGE_BLANK)
    @Pattern(regexp = AccountsConsts.LANGUAGE_REGEX, message = AccountMessages.LANGUAGE_REGEX_NOT_MET)
    @Column(name = DatabaseConsts.ACCOUNT_LANGUAGE_COLUMN, nullable = false, length = 16)
    @Setter
    private String accountLanguage;

    /**
     * User's phone number
     */
    @NotBlank(message = AccountMessages.PHONE_NUMBER_BLANK)
    @Pattern(regexp = AccountsConsts.PHONE_NUMBER_REGEX, message = AccountMessages.PHONE_NUMBER_REGEX_NOT_MET)
    @Column(name = DatabaseConsts.ACCOUNT_PHONE_NUMBER_COLUMN, nullable = false, length = 32)
    @Getter @Setter
    private String phoneNumber;

    @Column(name = DatabaseConsts.ACCOUNT_ACTIVATION_TIMESTAMP)
    @PastOrPresent(message = AccountMessages.ACTIVATION_TIMESTAMP_FUTURE)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private LocalDateTime activationTime;

    // Other fields - used for access control, and storing historical data

    /**
     * Time of the creation of the entity object in the database.
     * Basically, this time is saved when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATION_TIMESTAMP, nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = AccountMessages.CREATION_TIMESTAMP_FUTURE)
    private LocalDateTime creationTime;

    /**
     * Identity of the user creating entity object in the database.
     * Basically, this is user login taken from SecurityContext when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATED_BY, updatable = false)
    private String createdBy;

    /**
     * Time of the update of the entity object in the database.
     * Basically, this time is saved when updating object to the database.
     */
    @Column(name = DatabaseConsts.UPDATE_TIMESTAMP)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = AccountMessages.UPDATE_TIMESTAMP_FUTURE)
    private LocalDateTime updateTime;

    /**
     * Identity of the user updating entity object in the database.
     * Basically, this is user login taken from SecurityContext when updating object in the database.
     */
    @Column(name = DatabaseConsts.UPDATED_BY)
    @Setter
    private String updatedBy;

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
     * Constructs new Account entity, with version setting.
     *
     * @param login       Account's login.
     * @param password    Account's password.
     * @param name        Account owner's firstname.
     * @param lastname    Account owner's lastname.
     * @param email       Email connected with the account.
     * @param phoneNumber Phone number connected with the account.
     * @param version     Object version.
     */
    public Account(String login, String password, String name, String lastname, String email, String phoneNumber, Long version) {
        super(version);
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
        userLevel.setAccount(this);
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

    public void activateAccount(boolean firstActivation) {
        if (firstActivation) this.active = true;
        else this.suspended = false;
        this.activationTime = LocalDateTime.now();
    }

    /**
     * Blocks account and sets the time of applying the blockade.
     */
    public void blockAccount(boolean adminLock) {
        this.blocked = true;
        // When admin blocks the account property blockedTime is not set
        this.blockedTime = adminLock ? null : LocalDateTime.now();
    }

    /**
     * Unblocks the account and resets the time of applying the blockade and count of unsuccessful logins.
     */
    public void unblockAccount() {
        // Remove account blockade
        this.blocked = false;
        this.blockedTime = null;
        // Reset unsuccessful login counter
        this.getActivityLog().setUnsuccessfulLoginCounter(0);
    }

    /**
     * Method used to establish whether user account could be authenticated to.
     * If it is either blocked or not activated yet or suspended, then boolean flag of false is returned.
     * Otherwise, true is returned.
     * @return Boolean flag indicating whether account could be authenticated to.
     */
    public boolean couldAuthenticate() {
        return !this.getBlocked() && this.getActive() && !this.getSuspended();
    }

    /**
     * Equals method implementation used for comparing account
     * objects.
     *
     * @param object Object to compare to this object.
     * @return Boolean flag indicating whether objects are equal (true)
     * or not (false)
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object == null || getClass() != object.getClass()) return false;

        Account account = (Account) object;

        return new EqualsBuilder().append(login, account.login).isEquals();
    }

    /**
     * HashCode implementation fulfilling the equals and hashCode
     * contract.
     *
     * @return HashCode value of this object.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(login).toHashCode();
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the account object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("Login: ", login)
                .toString();
    }

    @PrePersist
    private void beforePersistingToTheDatabase() {
        this.creationTime = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.createdBy = authentication.getName();
        }
    }

    @PreUpdate
    private void beforeUpdatingInTheDatabase() {
        this.updateTime = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.updatedBy = authentication.getName();
        }
    }
}
