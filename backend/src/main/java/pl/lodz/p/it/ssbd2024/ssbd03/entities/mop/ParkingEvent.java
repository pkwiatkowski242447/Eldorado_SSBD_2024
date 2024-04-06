package pl.lodz.p.it.ssbd2024.ssbd03.entities.mop;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_event")
@ToString(callSuper = true)
@NoArgsConstructor
@Getter @Setter
public class ParkingEvent extends AbstractEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static enum EventType {ENTRY, EXIT}

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "reservation_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Reservation reservation;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;
}
