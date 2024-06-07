package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.TokenMessages;

import java.time.LocalDateTime;

@Entity
@Table(name = DatabaseConsts.ENTRY_CODE_TABLE)
@Getter
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = "EntryCode.findEntryCodeByReservationId",
                query = """
                        SELECT ec FROM EntryCode ec
                        WHERE ec.reservation.id = :reservationId"""
        )
})
public class EntryCode extends AbstractEntity {

    @Column(name = DatabaseConsts.ENTRY_CODE_VALUE_COLUMN, nullable = false)
    private String entryCode;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(
            name = DatabaseConsts.ENTRY_CODE_RESERVATION_ID_COLUMN,
            referencedColumnName = DatabaseConsts.PK_COLUMN,
            foreignKey = @ForeignKey(name = DatabaseConsts.ENTRY_CODE_RESERVATION_ID_FK),
            nullable = false, updatable = false, unique = true
    )
    private Reservation reservation;

    // Other fields - used for access control, and storing historical data

    /**
     * Time of the creation of the entity object in the database.
     * Basically, this time is saved when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATION_TIMESTAMP, nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @PastOrPresent(message = TokenMessages.CREATION_TIMESTAMP_FUTURE)
    private LocalDateTime creationTime;

    /**
     * Identity of the user creating entity object in the database.
     * Basically, this is user login taken from SecurityContext when persisting object to the database.
     */
    @Column(name = DatabaseConsts.CREATED_BY, updatable = false)
    private String createdBy;

    /**
     * Basic constructor for entry code entity object creation.
     * @param entryCode String value representing the generated entry code.
     * @param reservation Reservation object, related to the entry code value.
     */
    public EntryCode(String entryCode, Reservation reservation) {
        this.entryCode = entryCode;
        this.reservation = reservation;
    }

    @PrePersist
    private void beforePersistingToTheDatabase() {
        this.creationTime = LocalDateTime.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            this.createdBy = authentication.getName();
        }
    }
}
