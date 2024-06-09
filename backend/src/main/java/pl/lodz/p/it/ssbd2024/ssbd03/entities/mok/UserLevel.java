package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.ReservationMessages;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
        },
        indexes = {
                @Index(name = DatabaseConsts.USER_LEVEL_ACCOUNT_ID_INDEX, columnList = DatabaseConsts.USER_LEVEL_ACCOUNT_ID_COLUMN)
        }
)
@LoggerInterceptor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = DatabaseConsts.DISCRIMINATOR_COLUMN, discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@NamedQueries(value = {
        @NamedQuery(
                name = "UserLevel.findAllUserLevelsForGivenAccount",
                query = """
                        SELECT u FROM UserLevel u
                        WHERE u.account.login = :login
                        """
        ),
        @NamedQuery(
                name = "UserLevel.findGivenUserLevelsForGivenAccount",
                query = """
                        SELECT ul FROM UserLevel ul
                        WHERE ul.account.login = :login AND TYPE(ul) = :userLevel
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
    @JoinColumn(name = DatabaseConsts.USER_LEVEL_ACCOUNT_ID_COLUMN,
            referencedColumnName = DatabaseConsts.PK_COLUMN,
            foreignKey = @ForeignKey(name = DatabaseConsts.USER_LEVEL_ACCOUNT_ID_FK),
            nullable = false, updatable = false)
    @Setter
    private Account account;

    // Other fields - used for access control, and storing historical data

    /**
     * Time of the creation of the entity object in the database.
     * Basically, this time is saved when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATION_TIMESTAMP, nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = ReservationMessages.CREATION_TIMESTAMP_FUTURE)
    private LocalDateTime creationTime;

    /**
     * Identity of the user creating entity object in the database.
     * Basically, this is user login taken from SecurityContext when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATED_BY, updatable = false)
    private String createdBy;

    /**
     * Time of the update of the entity object in the database.
     * Basically, this time is saved when updating object to the database.
     */
    @Column(name = DatabaseConsts.UPDATE_TIMESTAMP)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = ReservationMessages.UPDATE_TIMESTAMP_FUTURE)
    @Setter
    private LocalDateTime updateTime;

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

    @PrePersist
    private void beforePersistingToTheDatabase() {
        this.creationTime = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.createdBy = authentication.getName();
        }
    }

    @PreUpdate
    private void beforeUpdatingInTheDatabase() {
        this.updateTime = LocalDateTime.now();
    }
}
