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

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = DatabaseConsts.ACCOUNT_LAST_SUCCESSFUL_LOGIN_TIME)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastSuccessfulLoginTime;

//    @Pattern(regexp = ActivityLogConsts.IPV4_REGEX, message = ActivityLogMessages.LAST_SUCCESSFUL_LOGIN_IP_NOT_VALID)
    @Column(name = DatabaseConsts.ACCOUNT_LAST_SUCCESSFUL_LOGIN_IP, length = 17)
    private String lastSuccessfulLoginIp;

    @Column(name = DatabaseConsts.ACCOUNT_LAST_UNSUCCESSFUL_LOGIN_TIME)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastUnsuccessfulLoginTime;

//    @Pattern(regexp = ActivityLogConsts.IPV4_REGEX, message = ActivityLogMessages.LAST_UNSUCCESSFUL_LOGIN_IP_NOT_VALID)
    @Column(name = DatabaseConsts.ACCOUNT_LAST_UNSUCCESSFUL_LOGIN_IP, length = 17)
    private String lastUnsuccessfulLoginIp;
}
