package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

@Entity
@Table(name = DatabaseConsts.ACCOUNT_TABLE)
@SecondaryTable(name = DatabaseConsts.PERSONAL_DATA_TABLE)
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class Account extends AbstractEntity {

    @NotBlank(message = AccountMessages.LOGIN_BLANK)
    @Pattern(regexp = AccountsConsts.LOGIN_REGEX, message = AccountMessages.LOGIN_REGEX_NOT_MET)
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
    private Boolean active = true;

    @NotNull(message = AccountMessages.BLOCKED_NULL)
    @Column(name = DatabaseConsts.ACCOUNT_BLOCKED_COLUMN, nullable = false)
    @Setter
    private Boolean blocked = false;

    @Column(name = DatabaseConsts.ACCOUNT_BLOCKED_TIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private LocalDateTime blockedTime;

    @NotBlank
    @Pattern(regexp = AccountsConsts.NAME_REGEX)
    @Size(min = AccountsConsts.NAME_MIN_LENGTH, message = AccountMessages.NAME_TOO_SHORT)
    @Size(max = AccountsConsts.NAME_MAX_LENGTH, message = AccountMessages.NAME_TOO_LONG)
    @Column(name = DatabaseConsts.PERSONAL_DATA_NAME_COLUMN, table = DatabaseConsts.PERSONAL_DATA_TABLE, nullable = false, length = 32)
    @Setter
    private String name;

    @NotBlank
    @Pattern(regexp = AccountsConsts.LASTNAME_REGEX)
    @Size(min = AccountsConsts.LASTNAME_MIN_LENGTH, message = AccountMessages.LASTNAME_TOO_SHORT)
    @Size(max = AccountsConsts.LASTNAME_MAX_LENGTH, message = AccountMessages.LASTNAME_TOO_LONG)
    @Column(name = DatabaseConsts.PERSONAL_DATA_LASTNAME_COLUMN, table = DatabaseConsts.PERSONAL_DATA_TABLE, nullable = false, length = 32)
    @Setter
    private String lastname;

    @Email(message = AccountMessages.EMAIL_NOT_MET)
    @Size(min = AccountsConsts.EMAIL_MIN_LENGTH, message = AccountMessages.EMAIL_TOO_SHORT)
    @Size(max = AccountsConsts.EMAIL_MAX_LENGTH, message = AccountMessages.EMAIL_TOO_LONG)
    @Column(name = DatabaseConsts.PERSONAL_DATA_EMAIL_COLUMN, table = DatabaseConsts.PERSONAL_DATA_TABLE, nullable = false, length = 64)
    @Setter
    private String email;

    @Size(min = AccountsConsts.USER_LEVEL_MIN_SIZE, message = AccountMessages.USER_LEVEL_EMPTY)
    @Size(max = AccountsConsts.USER_LEVEL_MAX_SIZE, message = AccountMessages.USER_LEVEL_FULL)
    @OneToMany(mappedBy = DatabaseConsts.ACCOUNT_TABLE, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    private final Collection<UserLevel> userLevels = new ArrayList<>();

    @Embedded
    @Setter
    private ActivityLog activityLog;

    @NotBlank(message = AccountMessages.LANGUAGE_BLANK)
    @Pattern(regexp = AccountsConsts.LANGUAGE_REGEX, message = AccountMessages.LANGUAGE_REGEX_NOT_MET)
    @Column(name = DatabaseConsts.ACCOUNT_LANGUAGE_COLUMN, nullable = false, length = 16)
    @Setter
    private String accountLanguage;

    @NotBlank(message = AccountMessages.PHONE_NUMBER_BLANK)
    @Pattern(regexp = AccountsConsts.PHONE_NUMBER_REGEX, message = AccountMessages.PHONE_NUMBER_REGEX_NOT_MET)
    @Column(name = DatabaseConsts.ACCOUNT_PHONE_NUMBER_COLUMN, nullable = false, length = 32)
    @Getter @Setter
    private String phoneNumber;

    @Column(name = DatabaseConsts.ACCOUNT_CREATION_DATE_COLUMN, nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private LocalDateTime creationDate;

    public Account(String login, String password, String name, String lastname, String email, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void addUserLevel(UserLevel userLevel) {
        userLevels.add(userLevel);
    }

    public void removeUserLevel(UserLevel userLevel) {
        userLevels.remove(userLevel);
    }

    public void blockAccount() {
        this.blocked = true;
        this.blockedTime = LocalDateTime.now();
    }

    public void unblockAccount() {
        this.blocked = false;
        this.blockedTime = null;
    }
}
