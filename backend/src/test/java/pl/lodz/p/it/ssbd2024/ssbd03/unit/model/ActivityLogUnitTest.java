package pl.lodz.p.it.ssbd2024.ssbd03.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.ActivityLog;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityLogUnitTest {

    private ActivityLog activityLog;

    private final int unsuccessfulLoginCounter = 2;
    private final LocalDateTime lastUnsuccessfulLoginTime = LocalDateTime.now().plusHours(12);
    private final String lastUnsuccessfulLoginIp = "172.16.0.1";
    private final LocalDateTime lastSuccessfulLoginTime = LocalDateTime.now().minusHours(12);
    private final String lastSuccessfulLoginIp = "192.168.0.1";

    @BeforeEach
    public void setUp() {
        activityLog = new ActivityLog();
        activityLog.setUnsuccessfulLoginCounter(this.unsuccessfulLoginCounter);
        activityLog.setLastUnsuccessfulLoginTime(this.lastUnsuccessfulLoginTime);
        activityLog.setLastUnsuccessfulLoginIp(this.lastUnsuccessfulLoginIp);
        activityLog.setLastSuccessfulLoginTime(this.lastSuccessfulLoginTime);
        activityLog.setLastSuccessfulLoginIp(this.lastSuccessfulLoginIp);
    }

    @Test
    public void activityLogNoArgsConstructorTestPositive() {
        ActivityLog testActivityLog = new ActivityLog();
        assertNotNull(testActivityLog);
    }

    @Test
    public void activityLogAllGettersTestPositive() {
        assertEquals(activityLog.getUnsuccessfulLoginCounter(), this.unsuccessfulLoginCounter);
        assertEquals(activityLog.getLastUnsuccessfulLoginTime(), this.lastUnsuccessfulLoginTime);
        assertEquals(activityLog.getLastUnsuccessfulLoginIp(), this.lastUnsuccessfulLoginIp);
        assertEquals(activityLog.getLastSuccessfulLoginTime(), this.lastSuccessfulLoginTime);
        assertEquals(activityLog.getLastSuccessfulLoginIp(), this.lastSuccessfulLoginIp);
    }

    @Test
    public void activityLogUnsuccessfulSetLoginCounterTestPositive() {
        int unsuccessfulLoginCounterBefore = activityLog.getUnsuccessfulLoginCounter();
        int newUnsuccessfulLoginCounter = 3;

        activityLog.setUnsuccessfulLoginCounter(newUnsuccessfulLoginCounter);

        int unsuccessfulLoginCounterAfter = activityLog.getUnsuccessfulLoginCounter();

        assertNotEquals(unsuccessfulLoginCounterBefore, newUnsuccessfulLoginCounter);
        assertNotEquals(unsuccessfulLoginCounterBefore, unsuccessfulLoginCounterAfter);
        assertEquals(newUnsuccessfulLoginCounter, unsuccessfulLoginCounterAfter);
    }

    @Test
    public void activityLogSetUnsuccessfulLoginTimeTestPositive() {
        LocalDateTime lastUnsuccessfulLoginTimeBefore = activityLog.getLastUnsuccessfulLoginTime();
        LocalDateTime newLastUnsuccessfulLoginTime = LocalDateTime.now();

        assertNotNull(lastUnsuccessfulLoginTimeBefore);
        assertNotNull(newLastUnsuccessfulLoginTime);

        activityLog.setLastUnsuccessfulLoginTime(newLastUnsuccessfulLoginTime);

        LocalDateTime lastUnsuccessfulLoginTimeAfter = activityLog.getLastUnsuccessfulLoginTime();
        assertNotNull(lastUnsuccessfulLoginTimeAfter);

        assertNotEquals(lastUnsuccessfulLoginTimeBefore, newLastUnsuccessfulLoginTime);
        assertNotEquals(lastUnsuccessfulLoginTimeBefore, lastUnsuccessfulLoginTimeAfter);
        assertEquals(newLastUnsuccessfulLoginTime, lastUnsuccessfulLoginTimeAfter);
    }

    @Test
    public void activityLogSetUnsuccessfulLoginIpTestPositive() {
        String lastUnsuccessfulLoginIpBefore = activityLog.getLastSuccessfulLoginIp();
        String newLastUnsuccessfulLoginIp = "10.16.0.0";

        assertNotNull(lastUnsuccessfulLoginIpBefore);
        assertNotNull(newLastUnsuccessfulLoginIp);

        activityLog.setLastSuccessfulLoginIp(newLastUnsuccessfulLoginIp);

        String lastUnsuccessfulLoginIpAfter = activityLog.getLastSuccessfulLoginIp();

        assertNotNull(lastUnsuccessfulLoginIpAfter);

        assertNotEquals(lastUnsuccessfulLoginIpBefore, newLastUnsuccessfulLoginIp);
        assertNotEquals(lastUnsuccessfulLoginIpBefore, lastUnsuccessfulLoginIpAfter);
        assertEquals(newLastUnsuccessfulLoginIp, lastUnsuccessfulLoginIpAfter);
    }

    @Test
    public void activityLogSetSuccessfulLoginTimeTestPositive() {
        LocalDateTime lastSuccessfulLoginTimeBefore = activityLog.getLastSuccessfulLoginTime();
        LocalDateTime newLastSuccessfulLoginTime = LocalDateTime.now();

        assertNotNull(lastSuccessfulLoginTimeBefore);
        assertNotNull(newLastSuccessfulLoginTime);

        activityLog.setLastSuccessfulLoginTime(newLastSuccessfulLoginTime);

        LocalDateTime lastSuccessfulLoginTimeAfter = activityLog.getLastSuccessfulLoginTime();
        assertNotNull(lastSuccessfulLoginTimeAfter);

        assertNotEquals(lastSuccessfulLoginTimeBefore, newLastSuccessfulLoginTime);
        assertNotEquals(lastSuccessfulLoginTimeBefore, lastSuccessfulLoginTimeAfter);
        assertEquals(newLastSuccessfulLoginTime, lastSuccessfulLoginTimeAfter);
    }

    @Test
    public void activityLogSetSuccessfulLoginIpTestPositive() {
        String lastSuccessfulLoginIpBefore = activityLog.getLastSuccessfulLoginIp();
        String newLastSuccessfulLoginIp = "10.31.255.0";

        assertNotNull(lastSuccessfulLoginIpBefore);
        assertNotNull(newLastSuccessfulLoginIp);

        activityLog.setLastSuccessfulLoginIp(newLastSuccessfulLoginIp);

        String lastSuccessfulLoginIpAfter = activityLog.getLastSuccessfulLoginIp();

        assertNotNull(lastSuccessfulLoginIpAfter);

        assertNotEquals(lastSuccessfulLoginIpBefore, newLastSuccessfulLoginIp);
        assertNotEquals(lastSuccessfulLoginIpBefore, lastSuccessfulLoginIpAfter);
        assertEquals(newLastSuccessfulLoginIp, lastSuccessfulLoginIpAfter);
    }

    @Test
    public void activityLogToStringTestPositive() {
        String result = activityLog.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.isBlank());
    }
}
