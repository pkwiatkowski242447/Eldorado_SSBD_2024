package pl.lodz.p.it.ssbd2024.ssbd03.entities;

import jakarta.persistence.*;
import lombok.*;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;

/**
 * Entity representing Token used in keeping track of various Account related actions.
 * @see Account
 */
@Entity
@Table(name = DatabaseConsts.TOKEN_TABLE)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
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


        )
})
public class Token extends AbstractEntity {
    /**
     * Used to specify the type of the Account action related to the token.
     */
    public static enum TokenType {REGISTER, RESET_PASSWORD, CONFIRM_EMAIL, CHANGE_OVERWRITTEN_PASSWORD}

    @Column(name = DatabaseConsts.TOKEN_TOKEN_VALUE_COLUMN, unique = true, nullable = false)
    @Setter
    private String tokenValue;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @ToString.Exclude
    private Account account;

    @Column(name = DatabaseConsts.TOKEN_TOKEN_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.RESET_PASSWORD;
}
