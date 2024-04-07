package pl.lodz.p.it.ssbd2024.ssbd03.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;

@Entity
@Table(name = "token")
@ToString(callSuper = true)
@NoArgsConstructor
public class Token extends AbstractEntity{
    @Column(name = "token_value", unique = true, nullable = false)
    @Getter @Setter
    private String tokenValue;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @ToString.Exclude
    @Getter
    private Account account;

    public static enum TokenType {RESET_PASSWORD}

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    private TokenType type = TokenType.RESET_PASSWORD;
}
