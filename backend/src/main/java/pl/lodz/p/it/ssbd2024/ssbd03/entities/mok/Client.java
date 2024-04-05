package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "client_data")
@DiscriminatorValue("CLIENT")
@ToString(callSuper = true)
@NoArgsConstructor
public class Client extends UserLevel implements Serializable {
    private static final long serialVersionUID = 1L;

}
