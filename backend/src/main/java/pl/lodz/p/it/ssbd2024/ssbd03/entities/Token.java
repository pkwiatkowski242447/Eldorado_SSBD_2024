package pl.lodz.p.it.ssbd2024.ssbd03.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;

@Entity
@Table(name = DatabaseConsts.TOKEN_TABLE)
@ToString(callSuper = true)
@NoArgsConstructor
public class Token extends AbstractEntity {
    public static enum TokenType {REGISTER, RESET_PASSWORD, CONFIRM_EMAIL, CHANGE_OVERWRITTEN_PASSWORD}

    @Column(name = DatabaseConsts.TOKEN_TOKEN_VALUE_COLUMN, unique = true, nullable = false)
    @Getter @Setter
    private String tokenValue;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @ToString.Exclude
    @Getter
    private Account account;

    @Column(name = DatabaseConsts.TOKEN_TOKEN_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    @Getter
    private TokenType type = TokenType.RESET_PASSWORD;
}
