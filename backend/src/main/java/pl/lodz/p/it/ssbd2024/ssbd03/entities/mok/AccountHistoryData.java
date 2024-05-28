package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity containing all information about any modification made to the user
 * account - such as the state of all the fields in the object, user that
 * made the change, time which the change took place at and type of the operation
 * performed on the account object.
 */
@Entity
@Table(name = DatabaseConsts.ACCOUNT_HIST_TABLE,
       uniqueConstraints = @UniqueConstraint(columnNames = {DatabaseConsts.ACCOUNT_HIST_ID_COLUMN, DatabaseConsts.ACCOUNT_HIST_VERSION_COLUMN})
)
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountHistoryData {

    @Id
    @Column(name = DatabaseConsts.ACCOUNT_HIST_ID_COLUMN, columnDefinition = "UUID", nullable = false, updatable = false)
    private UUID id;

    @Id
    @Column(name = DatabaseConsts.ACCOUNT_HIST_VERSION_COLUMN, nullable = false, updatable = false)
    private Long version;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_LOGIN_COLUMN, nullable = false, updatable = false, length = 32)
    private String login;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_PASSWORD_COLUMN, nullable = false, updatable = false, length = 60)
    private String password;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_VERIFIED_COLUMN, nullable = false, updatable = false)
    private Boolean verified;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_ACTIVE_COLUMN, nullable = false, updatable = false)
    private Boolean active;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_BLOCKED_COLUMN, nullable = false, updatable = false)
    private Boolean blocked;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_TWO_FACTOR_AUTH_COLUMN, nullable = false, updatable = false)
    private Boolean twoFactorAuth;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_BLOCKED_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime blockedTime;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_FIRST_NAME_COLUMN, nullable = false, length = 64, updatable = false)
    private String name;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_LAST_NAME_COLUMN, nullable = false, length = 64, updatable = false)
    private String lastname;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_EMAIL_COLUMN, nullable = false, length = 64, updatable = false)
    private String email;

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
    private Integer unsuccessfulLoginCounter = 0;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_LANGUAGE_COLUMN, nullable = false, length = 2, updatable = false)
    private String language;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_OPERATION_TYPE_COLUMN, nullable = false, length = 64)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = DatabaseConsts.ACCOUNT_HIST_MODIFICATION_TIME_COLUMN, nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modificationTime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = DatabaseConsts.ACCOUNT_HIST_MODIFIED_BY_COLUMN, updatable = false)
    private Account modifiedBy;

    @PrePersist
    @PreUpdate
    @PreRemove
    private void setModificationTime() {
        modificationTime = LocalDateTime.now();
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
