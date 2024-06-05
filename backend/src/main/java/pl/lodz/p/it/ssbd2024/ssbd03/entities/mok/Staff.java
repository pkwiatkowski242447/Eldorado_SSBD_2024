package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity representing Account's staff access level.
 */
@Entity
@Table(
        name = DatabaseConsts.STAFF_DATA_TABLE,
        indexes = {
                @Index(name = DatabaseConsts.STAFF_DATA_USER_LEVEL_ID_INDEX, columnList = DatabaseConsts.PK_COLUMN)
        }
)
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = DatabaseConsts.STAFF_DATA_USER_LEVEL_ID_FK))
@DiscriminatorValue(value = DatabaseConsts.STAFF_DISCRIMINATOR)
@LoggerInterceptor
@ToString(callSuper = true)
@NoArgsConstructor
public class Staff extends UserLevel implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public Staff(Long version) {
        super(version);
    }
}
