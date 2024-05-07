package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity representing Account's admin access level.
 * @see Account
 * @see UserLevel
 */
@Entity
@Table(name = DatabaseConsts.ADMIN_DATA_TABLE)
@DiscriminatorValue(value = DatabaseConsts.ADMIN_DISCRIMINATOR)
@ToString(callSuper = true)
@NoArgsConstructor
public class Admin extends UserLevel implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;
}
