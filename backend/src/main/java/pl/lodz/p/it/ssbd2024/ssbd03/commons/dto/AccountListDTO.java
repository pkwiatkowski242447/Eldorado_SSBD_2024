package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountListDTO {
    private UUID id;
    private String login;
    private String name;
    private String lastName;
    private boolean active;
    private boolean blocked;
    private boolean verified;
    private LocalDateTime lastSuccessfulLoginTime;
    private LocalDateTime lastUnsuccessfulLoginTime;
    private List<String> userLevels;
}
