package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.ClientMessages;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity representing Account's client access level. Additionally, it stores information about client type.
 *
 * @see Account
 * @see UserLevel
 */
@Entity
@Table(
        name = DatabaseConsts.CLIENT_DATA_TABLE,
        indexes = {
                @Index(name = DatabaseConsts.CLIENT_DATA_USER_LEVEL_ID_INDEX, columnList = DatabaseConsts.PK_COLUMN)
        }
)
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = DatabaseConsts.CLIENT_DATA_USER_LEVEL_ID_FK))
@DiscriminatorValue(value = DatabaseConsts.CLIENT_DISCRIMINATOR)
@LoggerInterceptor
@NoArgsConstructor
public class Client extends UserLevel implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Represents different client types recognised in the system.
     */
    public static enum ClientType {BASIC, STANDARD, PREMIUM}


    /**
     * Type of the client, indicating its level in the system.
     */
    @NotNull(message = ClientMessages.CLIENT_TYPE_NULL)
    @Column(name = DatabaseConsts.CLIENT_DATA_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private ClientType type = ClientType.BASIC;

    public Client(Long version) {
        super(version);
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the client object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("type", type)
                .toString();
    }
}
