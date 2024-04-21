package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule;

public class ScheduleException extends Exception {

    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
