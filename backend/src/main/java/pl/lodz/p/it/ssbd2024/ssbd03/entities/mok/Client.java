package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Table(name = DatabaseConsts.CLIENT_DATA_TABLE)
@DiscriminatorValue(value = DatabaseConsts.CLIENT_DISCRIMINATOR)
@ToString(callSuper = true)
@NoArgsConstructor
public class Client extends UserLevel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Represents different client types recognised in the system.
     */
    public static enum ClientType {BASIC, STANDARD, PREMIUM}

    @NotNull(message = ClientMessages.CLIENT_TYPE_NULL)
    @Column(name = DatabaseConsts.CLIENT_DATA_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private ClientType type = ClientType.BASIC;
}
