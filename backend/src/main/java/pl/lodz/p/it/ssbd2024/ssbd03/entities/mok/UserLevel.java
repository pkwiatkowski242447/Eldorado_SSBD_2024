package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity used as a basis for all access levels in the system. It's used to bind roles to the Account.
 *
 * @see Account
 * @see Admin
 * @see Client
 * @see Staff
 */
@Entity
@Table(
        name = DatabaseConsts.USER_LEVEL_TABLE,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {DatabaseConsts.USER_LEVEL_ACCOUNT_ID_COLUMN, DatabaseConsts.DISCRIMINATOR_COLUMN})
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = DatabaseConsts.DISCRIMINATOR_COLUMN, discriminatorType = DiscriminatorType.STRING)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserLevel extends AbstractEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = DatabaseConsts.USER_LEVEL_ACCOUNT_ID_COLUMN, referencedColumnName = DatabaseConsts.PK_COLUMN, nullable = false, updatable = false)
    @Getter
    @Setter
    private Account account;

}
