package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "user_level", uniqueConstraints = {@UniqueConstraint(columnNames = {"account_id", "level"})})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "level", discriminatorType = DiscriminatorType.STRING)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserLevel extends AbstractEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
    @Getter @Setter
    private Account account;

}
