package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@ToString
@Getter @Setter
public class ActivityLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "last_successful_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastSuccessfulLoginTime;

    @Column(name = "last_successful_login_ip", length = 17)
    private String lastSuccessfulLoginIp;

    @Column(name = "last_unsuccessful_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastUnsuccessfulLoginTime;

    @Column(name = "last_unsuccessful_login_ip", length = 17)
    private String lastUnsuccessfulLoginIp;
}
