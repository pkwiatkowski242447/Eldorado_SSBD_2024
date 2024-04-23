package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule;

/**
 * Used to specify an Exception related with incorrect properties passed to the scheduler.
 */
public class ScheduleBadProperties extends ScheduleException {
    public ScheduleBadProperties(String message) {
        super(message);
    }

    public ScheduleBadProperties(String message, Throwable cause) {
        super(message, cause);
    }
}
