package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mop.ParkingEventMessages;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity representing and storing information about entries and exits within a Reservation.
 *
 * @see Reservation
 */
@Entity
@Table(name = DatabaseConsts.PARKING_EVENT_TABLE)
@NoArgsConstructor
public class ParkingEvent extends AbstractEntity implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Used to describe the type of the ParkingEvent
     */
    public enum EventType {ENTRY, EXIT}

    /**
     * The reservation associated with the parking event.
     */
    @NotNull(message = ParkingEventMessages.RESERVATION_NULL)
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = DatabaseConsts.PARKING_EVENT_RESERVATION_ID_COLUMN, referencedColumnName = DatabaseConsts.PK_COLUMN, nullable = false, updatable = false)
    @Getter
    @Setter
    private Reservation reservation;

    /**
     * The date and time of the parking event.
     */
    @NotNull(message = ParkingEventMessages.DATE_NULL)
    @Column(name = DatabaseConsts.PARKING_EVENT_DATE_COLUMN, nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private LocalDateTime date;

    /**
     * The type of the parking event (ENTRY or EXIT).
     */
    @NotNull(message = ParkingEventMessages.EVENT_TYPE_NULL)
    @Column(name = DatabaseConsts.PARKING_EVENT_TYPE_COLUMN, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    private EventType type;

    /**
     *
     * Constructs a new parkingEvent.
     *
     * @param time ParkingEvent's time
     * @param type ParkingEvent's type
     */
    public ParkingEvent(LocalDateTime time, EventType type) {
        this.date = time;
        this.type = type;
    }

    /**
     * Custom toString() method implementation that
     * does not return any information relating to the business
     * data.
     *
     * @return String representation of the parking event object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .toString();
    }
}
