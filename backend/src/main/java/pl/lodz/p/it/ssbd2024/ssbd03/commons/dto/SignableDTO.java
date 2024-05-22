package pl.lodz.p.it.ssbd2024.ssbd03.commons.dto;

import java.util.Map;

/**
 * Interface used for ensuring ability of signing
 */
public interface SignableDTO {

    /**
     * This method returns properties (names and values) that should be signed.
     * @return Returns map of properties that should be signed.
     */
    Map<String, ?> getSigningFields();
}