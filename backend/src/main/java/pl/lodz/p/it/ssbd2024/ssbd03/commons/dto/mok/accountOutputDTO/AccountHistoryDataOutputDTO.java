package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok.accountOutputDTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data transfer object used in returning account history data.
 */
@Getter
@Setter
@NoArgsConstructor
@LoggerInterceptor
public class AccountHistoryDataOutputDTO {
    @Schema(description = "UUID identifier linked with account", example = "73538016-095a-4564-965c-9a17c9ded334", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;
    @Schema(description = "Version of the account data", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long version;
    @Schema(description = "Login of the account", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;
    @Schema(description = "Flag indicating if account is suspended", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean suspended;
    @Schema(description = "Flag indicating if account is active", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean active;
    @Schema(description = "Flag indicating if account is blocked", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean blocked;
    @Schema(description = "Flag indicating if account has 2FA enabled", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean twoFactorAuth;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "Data and time when account has been blocked", example = "YYYY-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime blockedTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "Data and time of last successful login in account", example = "YYYY-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime lastSuccessfulLoginTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "Data and time of last unsuccessful login in account", example = "YYYY-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime lastUnsuccessfulLoginTime;
    @Schema(description = "Language used natively in user browser", example = "pl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountLanguage;
    @Schema(description = "IPv4 address of last successful login in account", example = "XXX.XXX.XXX.XXX", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastSuccessfulLoginIp;
    @Schema(description = "IPv4 address of last unsuccessful login in account", example = "XXX.XXX.XXX.XXX", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastUnsuccessfulLoginIp;
    @Schema(description = "Phone number of the user", example = "111222333", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phoneNumber;
    @Schema(description = "Last name of the user", example = "Chrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastname;
    @Schema(description = "First name of the user", example = "Boleslaw", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "E-mail used by the user", example = "boleslawchrobry@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(description = "Internationalization key for the type of operation made on that entry", example = "operation.type.login", requiredMode = Schema.RequiredMode.REQUIRED)
    private String operationType;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "Data and time of modification of the account", example = "YYYY-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime modificationTime;
    @Schema(description = "Login of the account that made the change, may be null in case the change can't be tracked to a user", example = "boleslawchrobry", requiredMode = Schema.RequiredMode.REQUIRED)
    private String modifiedBy;

    /***
     * All arguments constructor for AccountHistoryDataOutputDTO - with calling constructor of superclass.
     */
    public AccountHistoryDataOutputDTO(UUID id, Long version,
                                       String login, boolean suspended,
                                       boolean active, boolean blocked,
                                       boolean twoFactorAuth, LocalDateTime blockedTime,
                                       LocalDateTime lastSuccessfulLoginTime, LocalDateTime lastUnsuccessfulLoginTime,
                                       String accountLanguage, String lastSuccessfulLoginIp,
                                       String lastUnsuccessfulLoginIp, String phoneNumber,
                                       String lastname, String name,
                                       String email, String operationType,
                                       LocalDateTime modificationTime, String modifiedBy) {
        this.id = id;
        this.version = version;
        this.login = login;
        this.suspended = suspended;
        this.active = active;
        this.blocked = blocked;
        this.twoFactorAuth = twoFactorAuth;
        this.blockedTime = blockedTime;
        this.lastSuccessfulLoginTime = lastSuccessfulLoginTime;
        this.lastUnsuccessfulLoginTime = lastUnsuccessfulLoginTime;
        this.accountLanguage = accountLanguage;
        this.lastSuccessfulLoginIp = lastSuccessfulLoginIp;
        this.lastUnsuccessfulLoginIp = lastUnsuccessfulLoginIp;
        this.phoneNumber = phoneNumber;
        this.lastname = lastname;
        this.name = name;
        this.email = email;
        this.operationType = operationType;
        this.modificationTime = modificationTime;
        this.modifiedBy = modifiedBy;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AccountHistoryDataOutputDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Id: ", id).append(super.toString()).toString();
    }
}
