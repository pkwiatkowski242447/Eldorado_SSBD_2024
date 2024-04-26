package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.accountOutputDTO.AccountAbstractOutputDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ToString
@Getter
public class AccountOutputDTO {
    private UUID id;
    private String login;
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
    private List<AccountAbstractOutputDTO> rolesDetails;
}
