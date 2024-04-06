package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "admin_data")
@DiscriminatorValue("ADMIN")
@ToString(callSuper = true)
@NoArgsConstructor
public class Admin extends UserLevel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

}
