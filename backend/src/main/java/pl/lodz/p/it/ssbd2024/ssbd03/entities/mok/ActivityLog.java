package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.ActivityLogConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.ActivityLogMessages;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Stores information about activity of an Account.
 * @see Account
 */
@Embeddable
@NoArgsConstructor
@ToString
@Getter @Setter
public class ActivityLog implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Timestamp of the last successful login.
     */
    @Column(name = DatabaseConsts.ACCOUNT_LAST_SUCCESSFUL_LOGIN_TIME)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastSuccessfulLoginTime;

    /**
     * IP address of the last successful login.
     */
    @Pattern(regexp = ActivityLogConsts.IPV4_REGEX, message = ActivityLogMessages.LAST_SUCCESSFUL_LOGIN_IP_NOT_VALID)
    @Column(name = DatabaseConsts.ACCOUNT_LAST_SUCCESSFUL_LOGIN_IP, length = 17)
    private String lastSuccessfulLoginIp;

    /**
     * Timestamp of the last unsuccessful login.
     */
    @Column(name = DatabaseConsts.ACCOUNT_LAST_UNSUCCESSFUL_LOGIN_TIME)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastUnsuccessfulLoginTime;

    /**
     * IP address of the last unsuccessful login.
     */
    @Pattern(regexp = ActivityLogConsts.IPV4_REGEX, message = ActivityLogMessages.LAST_UNSUCCESSFUL_LOGIN_IP_NOT_VALID)
    @Column(name = DatabaseConsts.ACCOUNT_LAST_UNSUCCESSFUL_LOGIN_IP, length = 17)
    private String lastUnsuccessfulLoginIp;

    /**
     * Counter for the number of unsuccessful login attempts.
     */
    @Column(name = DatabaseConsts.UNSUCCESSFUL_LOGIN_COUNTER)
    private Integer unsuccessfulLoginCounter = 0;
}
