package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime lastSuccessfulLoginTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime lastUnsuccessfulLoginTime;
    private List<String> userLevels;

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the AccountListDTO object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Id: ", id)
                .append("Login: ", login)
                .toString();
    }
}
