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


        ),
        @NamedQuery(
                name = "Token.findByTokenType",
                query = """
                        SELECT t FROM Token t
                        WHERE t.type = :tokenType"""
        )
})
public class Token extends AbstractEntity {
    /**
     * Used to specify the type of the Account action related to the token.
     */
    public enum TokenType {
        MULTI_FACTOR_AUTHENTICATION_CODE,
        REGISTER,
        RESET_PASSWORD,
        CONFIRM_EMAIL,
        CHANGE_OVERWRITTEN_PASSWORD
    }

    /**
     * The value of the token.
     */
    @Column(name = DatabaseConsts.TOKEN_TOKEN_VALUE_COLUMN, unique = true, nullable = false, length = 512)
    @Getter @Setter
    private String tokenValue;

    /**
     * The account associated with this token.
     */
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @ToString.Exclude
    private Account account;

    /**
     * The type of the token, specifying the type of account action related to the token.
     */
    @Column(name = DatabaseConsts.TOKEN_TOKEN_TYPE_COLUMN, nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.RESET_PASSWORD;
}
