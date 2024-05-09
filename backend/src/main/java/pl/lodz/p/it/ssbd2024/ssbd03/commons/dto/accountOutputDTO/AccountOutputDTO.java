package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.AccountSignableDTO;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@ToString(callSuper = true)
@Getter
public class AccountOutputDTO extends AccountSignableDTO {
    private UUID id;
    private boolean verified;
    private boolean active;
    private boolean blocked;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime blockedTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime creationDate;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime lastSuccessfulLoginTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime lastUnsuccessfulLoginTime;
    private String accountLanguage;
    private String lastSuccessfulLoginIp;
    private String lastUnsuccessfulLoginIp;
    private String phoneNumber;
    private String lastname;
    private String name;
    private String email;

    public AccountOutputDTO(String login, Long version, Set<UserLevelDTO> userLevelsDto, UUID id, boolean verified,
                            boolean active, boolean blocked, LocalDateTime blockedTime, LocalDateTime creationDate,
                            LocalDateTime lastSuccessfulLoginTime, LocalDateTime lastUnsuccessfulLoginTime,
                            String accountLanguage, String lastSuccessfulLoginIp, String lastUnsuccessfulLoginIp,
                            String phoneNumber, String lastname, String name, String email) {
        super(login, version, userLevelsDto);
        this.id = id;
        this.verified = verified;
        this.active = active;
        this.blocked = blocked;
        this.blockedTime = blockedTime;
        this.creationDate = creationDate;
        this.lastSuccessfulLoginTime = lastSuccessfulLoginTime;
        this.lastUnsuccessfulLoginTime = lastUnsuccessfulLoginTime;
        this.accountLanguage = accountLanguage;
        this.lastSuccessfulLoginIp = lastSuccessfulLoginIp;
        this.lastUnsuccessfulLoginIp = lastUnsuccessfulLoginIp;
        this.phoneNumber = phoneNumber;
        this.lastname = lastname;
        this.name = name;
        this.email = email;
    }
}
