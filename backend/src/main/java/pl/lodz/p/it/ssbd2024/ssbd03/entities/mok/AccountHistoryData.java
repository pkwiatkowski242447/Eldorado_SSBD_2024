package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.AccountHistoryDataConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AccountHistoryDataMessages;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity containing all information about any modification made to the user
 * account - such as the state of all the fields in the object, user that
 * made the change, time which the change took place at and type of the operation
 * performed on the account object.
 */
@Entity
@Table(
        name = DatabaseConsts.ACCOUNT_HIST_TABLE,
        uniqueConstraints = @UniqueConstraint(columnNames = {DatabaseConsts.ACCOUNT_HIST_ID_COLUMN, DatabaseConsts.ACCOUNT_HIST_VERSION_COLUMN}),
        indexes = {
                @Index(name = DatabaseConsts.ACCOUNT_HIST_ACCOUNT_ID_INDEX ,columnList = DatabaseConsts.ACCOUNT_HIST_MODIFIED_BY_COLUMN)
        }
)
@LoggerInterceptor
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedQueries({
        // General queries
        @NamedQuery(
                name = "AccountHistoryData.findByAccountId",
                query = """
                        SELECT a FROM AccountHistoryData a
                        WHERE a.id = :id
                        ORDER BY a.modificationTime DESC
                        """
        ),
        @NamedQuery(
                name = "AccountHistoryData.findAll",
                query = """
                        SELECT a FROM AccountHistoryData a
                        ORDER BY a.login
                        """
        ),
        @NamedQuery(
                name = "AccountHistoryData.checkIfEntityExists",
                query = """
                        SELECT 1 FROM AccountHistoryData a
                        WHERE a.id = :id AND a.version = :version
                        """
        )
})
public class AccountHistoryData {

    @Id
    @NotNull(message = AccountHistoryDataMessages.ID_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_ID_COLUMN, columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @Id
    @NotNull(message = AccountHistoryDataMessages.VERSION_NULL)
    @PositiveOrZero(message = AccountHistoryDataMessages.VERSION_LESS_THAN_ZERO)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_VERSION_COLUMN, nullable = false, updatable = false)
    private Long version;

    @NotBlank(message = AccountHistoryDataMessages.LOGIN_BLANK)
    @Pattern(regexp = AccountHistoryDataConsts.LOGIN_REGEX, message = AccountHistoryDataMessages.LOGIN_REGEX_NOT_MET)
    @Size(min = AccountHistoryDataConsts.LOGIN_MIN_LENGTH, message = AccountHistoryDataMessages.LOGIN_TOO_SHORT)
    @Size(max = AccountHistoryDataConsts.LOGIN_MAX_LENGTH, message = AccountHistoryDataMessages.LOGIN_TOO_LONG)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_LOGIN_COLUMN, nullable = false, updatable = false, length = 32)
    private String login;

    @NotBlank(message = AccountHistoryDataMessages.PASSWORD_BLANK)
    @Size(min = AccountHistoryDataConsts.PASSWORD_LENGTH, max = AccountHistoryDataConsts.PASSWORD_LENGTH, message = AccountHistoryDataMessages.PASSWORD_INVALID_LENGTH)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_PASSWORD_COLUMN, nullable = false, updatable = false, length = 60)
    private String password;

    @NotNull(message = AccountHistoryDataMessages.SUSPENDED_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_SUSPENDED_COLUMN, nullable = false, updatable = false)
    private Boolean suspended;

    @NotNull(message = AccountHistoryDataMessages.ACTIVE_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_ACTIVE_COLUMN, nullable = false, updatable = false)
    private Boolean active;

    @NotNull(message = AccountHistoryDataMessages.BLOCKED_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_BLOCKED_COLUMN, nullable = false, updatable = false)
    private Boolean blocked;

    @NotNull(message = AccountHistoryDataMessages.TWO_FACTOR_AUTH_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_TWO_FACTOR_AUTH_COLUMN, nullable = false, updatable = false)
    private Boolean twoFactorAuth;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_BLOCKED_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime blockedTime;

    @NotBlank(message = AccountHistoryDataMessages.NAME_BLANK)
    @Pattern(regexp = AccountHistoryDataConsts.NAME_REGEX, message = AccountHistoryDataMessages.NAME_REGEX_NOT_MET)
    @Size(min = AccountHistoryDataConsts.NAME_MIN_LENGTH, message = AccountHistoryDataMessages.NAME_TOO_SHORT)
    @Size(max = AccountHistoryDataConsts.NAME_MAX_LENGTH, message = AccountHistoryDataMessages.NAME_TOO_LONG)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_FIRST_NAME_COLUMN, nullable = false, length = 64, updatable = false)
    private String name;

    @NotBlank(message = AccountHistoryDataMessages.LASTNAME_BLANK)
    @Pattern(regexp = AccountHistoryDataConsts.LASTNAME_REGEX, message = AccountHistoryDataMessages.LASTNAME_REGEX_NOT_MET)
    @Size(min = AccountHistoryDataConsts.LASTNAME_MIN_LENGTH, message = AccountHistoryDataMessages.LASTNAME_TOO_SHORT)
    @Size(max = AccountHistoryDataConsts.LASTNAME_MAX_LENGTH, message = AccountHistoryDataMessages.LASTNAME_TOO_LONG)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_LAST_NAME_COLUMN, nullable = false, length = 64, updatable = false)
    private String lastname;

    @Email(message = AccountHistoryDataMessages.EMAIL_CONSTRAINT_NOT_MET)
    @Size(min = AccountHistoryDataConsts.EMAIL_MIN_LENGTH, message = AccountHistoryDataMessages.EMAIL_TOO_SHORT)
    @Size(max = AccountHistoryDataConsts.EMAIL_MAX_LENGTH, message = AccountHistoryDataMessages.EMAIL_TOO_LONG)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_EMAIL_COLUMN, nullable = false, length = 64, updatable = false)
    private String email;

    @NotBlank(message = AccountHistoryDataMessages.PHONE_NUMBER_BLANK)
    @Pattern(regexp = AccountHistoryDataConsts.PHONE_NUMBER_REGEX, message = AccountHistoryDataMessages.PHONE_NUMBER_REGEX_NOT_MET)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_PHONE_NUMBER_COLUMN, nullable = false, length = 16, updatable = false)
    private String phoneNumber;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_LAST_SUCCESSFUL_LOGIN_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastSuccessfulLoginTime;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_LAST_SUCCESSFUL_LOGIN_IP_COLUMN, length = 17, updatable = false)
    private String lastSuccessfulLoginIp;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_LAST_UNSUCCESSFUL_LOGIN_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastUnsuccessfulLoginTime;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_LAST_UNSUCCESSFUL_LOGIN_IP_COLUMN, length = 17, updatable = false)
    private String lastUnsuccessfulLoginIp;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_UNSUCCESSFUL_LOGIN_COUNTER_COLUMN, nullable = false)
    private Integer unsuccessfulLoginCounter;

    @NotBlank(message = AccountHistoryDataMessages.LANGUAGE_BLANK)
    @Pattern(regexp = AccountHistoryDataConsts.LANGUAGE_REGEX, message = AccountHistoryDataMessages.LANGUAGE_REGEX_NOT_MET)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_LANGUAGE_COLUMN, nullable = false, length = 2, updatable = false)
    private String language;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_OPERATION_TYPE_COLUMN, nullable = false, length = 64)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @NotNull(message = AccountHistoryDataMessages.MODIFICATION_TIME_NULL)
    @PastOrPresent(message = AccountHistoryDataMessages.MODIFICATION_TIME_FUTURE)
    @Column(name = DatabaseConsts.ACCOUNT_HIST_MODIFICATION_TIME_COLUMN, nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modificationTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = DatabaseConsts.ACCOUNT_HIST_MODIFIED_BY_COLUMN,
            referencedColumnName = DatabaseConsts.PK_COLUMN,
            foreignKey = @ForeignKey(name = DatabaseConsts.ACCOUNT_HIST_ACCOUNT_ID_FK),
            updatable = false
    )
    private Account modifiedBy;

    @PrePersist
    @PreUpdate
    @PreRemove
    private void setModificationTime() {
        this.modificationTime = LocalDateTime.now();
    }

    public AccountHistoryData(Account account, OperationType operationType, Account modifiedBy) {
        this.id = account.getId();
        this.version = account.getVersion();
        this.login = account.getLogin();
        this.password = account.getPassword();
        this.suspended = account.getSuspended();
        this.active = account.getActive();
        this.blocked = account.getBlocked();
        this.twoFactorAuth = account.getTwoFactorAuth();
        this.blockedTime = account.getBlockedTime();
        this.name = account.getName();
        this.lastname = account.getLastname();
        this.email = account.getEmail();
        this.phoneNumber = account.getPhoneNumber();
        this.lastSuccessfulLoginTime = account.getActivityLog().getLastSuccessfulLoginTime();
        this.lastSuccessfulLoginIp = account.getActivityLog().getLastSuccessfulLoginIp();
        this.lastUnsuccessfulLoginTime = account.getActivityLog().getLastUnsuccessfulLoginTime();
        this.lastUnsuccessfulLoginIp = account.getActivityLog().getLastUnsuccessfulLoginIp();
        this.unsuccessfulLoginCounter = account.getActivityLog().getUnsuccessfulLoginCounter();
        this.language = account.getAccountLanguage();
        this.operationType = operationType;
        this.modifiedBy = modifiedBy;
    }

    /**
     * Custom toString() method implementation, defined in order
     * to avoid potential leaks of business data to the logs.
     * @return String representation of the AccountHistoryData
     * object without any sensitive data.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Id: ", id)
                .append("Version: ", version)
                .append("Login: ", login)
                .toString();
    }
}
