package pl.lodz.p.it.ssbd2024.ssbd03.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EnhancedLink extends Link {

    private String identifier;
    private String useCaseName;
    private String methodName;

    public EnhancedLink(String href,
                        LinkRelation relation,
                        String identifier,
                        String useCaseName,
                        String methodName) {
        super(href, relation);
        this.identifier = identifier;
        this.useCaseName = useCaseName;
        this.methodName = methodName;
    }
}
