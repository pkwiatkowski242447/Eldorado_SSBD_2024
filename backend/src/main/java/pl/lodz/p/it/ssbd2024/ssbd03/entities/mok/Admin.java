package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity representing Account's admin access level.
 * @see Account
 * @see UserLevel
 */
@Entity
@Table(
        name = DatabaseConsts.ADMIN_DATA_TABLE,
        indexes = {
                @Index(name = DatabaseConsts.ADMIN_DATA_USER_LEVEL_ID_INDEX, columnList = DatabaseConsts.PK_COLUMN)
        }
)
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = DatabaseConsts.ADMIN_DATA_USER_LEVEL_ID_FK))
@DiscriminatorValue(value = DatabaseConsts.ADMIN_DISCRIMINATOR)
@LoggerInterceptor
@ToString(callSuper = true)
@NoArgsConstructor
public class Admin extends UserLevel implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public Admin(Long version) {
        super(version);
    }
}
