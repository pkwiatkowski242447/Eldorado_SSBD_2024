package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.ClientMessages;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = DatabaseConsts.CLIENT_DATA_TABLE)
@DiscriminatorValue(value = DatabaseConsts.CLIENT_DISCRIMINATOR)
@ToString(callSuper = true)
@NoArgsConstructor
public class Client extends UserLevel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static enum ClientType {BASIC, STANDARD, PREMIUM}

    @NotNull(message = ClientMessages.CLIENT_TYPE_NULL)
    @Column(name = DatabaseConsts.CLIENT_DATA_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private ClientType type = ClientType.BASIC;
}
