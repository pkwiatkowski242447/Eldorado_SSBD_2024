package pl.lodz.p.it.ssbd2024.ssbd03.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.DatabaseConsts;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.messages.mok.AttributeMessages;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = DatabaseConsts.ATTRIBUTE_NAME_TABLE,
        uniqueConstraints = @UniqueConstraint(columnNames = {DatabaseConsts.ATTRIBUTE_NAME_COLUMN})
)
@Getter
@LoggerInterceptor
@NoArgsConstructor
@AllArgsConstructor
public class AttributeName extends AbstractEntity implements Serializable {

    /**
     * Unique identifier for serialization purposes.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Name of the attribute.
     */
    @NotBlank(message = AttributeMessages.ATTRIBUTE_NAME_BLANK)
    @Column(name = DatabaseConsts.ATTRIBUTE_NAME_COLUMN, nullable = false, updatable = false)
    @Setter
    private String attributeName;

    @OneToMany(mappedBy = "attributeNameId", cascade = CascadeType.ALL)
    private final List<AttributeValue> listOfAttributeValues = new ArrayList<>();

    /**
     * Custom toString() method implementation, implemented in order
     * not to leak any business data.
     * @return String representation of the object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
