package pl.lodz.p.it.ssbd2024.ssbd03.entities;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;

@Entity
@Table(name = DatabaseConsts.TOKEN_TABLE)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = "Token.findByTypeAndAccount",
                query = """
                        SELECT t FROM Token t
                        WHERE t.type = :tokenType AND t.account.id = :accountId"""
        ),
        @NamedQuery(
                name = "Token.removeByAccount",
                query = """
                        DELETE FROM Token t
                        WHERE t.account.id = :accountId"""
        ),
        @NamedQuery(
                name = "Token.removeByTypeAndAccount",
                query = """
                        DELETE FROM Token t
                        WHERE t.type = :tokenType AND t.account.id = :accountId"""
        ),
        @NamedQuery(
                name = "Token.findByTokenValue",
                query = """
                        SELECT t FROM Token t
                        WHERE t.tokenValue = :tokenValue"""


        ),
        @NamedQuery(
                name = "Token.findByTokenType",
                query = """
                        SELECT t FROM Token t
                        WHERE t.type = :tokenType"""
        )
})

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
