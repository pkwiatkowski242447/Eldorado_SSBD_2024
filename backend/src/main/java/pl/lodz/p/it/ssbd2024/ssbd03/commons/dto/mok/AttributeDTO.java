package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto.mok;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;

@Getter
@Setter
@NoArgsConstructor
@LoggerInterceptor
public class AttributeDTO {
    private String attributeName;
    private String attributeValue;

    public AttributeDTO(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
}
