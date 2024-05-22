package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActivityLogUnitTest {

    private ActivityLog activityLog;
    @BeforeEach
    public void setUp() {
        activityLog = new ActivityLog();

    }
    @Test
    public void testToString() {
        String result = activityLog.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
    }
}
