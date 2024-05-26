package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedQueries(value = {
        @NamedQuery(
                name = "UserLevel.findAllUserLevelsForGivenAccount",
                query = """
                        SELECT u FROM UserLevel u
                        WHERE u.account.id = :accountId
                        """
        ),
        @NamedQuery(
                name = "UserLevel.findGivenUserLevelsForGivenAccount",
                query = """
                        SELECT ul FROM UserLevel ul
                        WHERE ul.account.id = :accountId AND TYPE(ul) = :userLevel
                        """
        )
})
public abstract class UserLevel extends AbstractEntity implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The account associated with this user level.
     */
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = DatabaseConsts.USER_LEVEL_ACCOUNT_ID_COLUMN, referencedColumnName = DatabaseConsts.PK_COLUMN, nullable = false, updatable = false)
    @Getter
    @Setter
    private Account account;

    public UserLevel(Long version) {
        super(version);
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the user level object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .toString();
    }
}
