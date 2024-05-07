package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule.ScheduleBadPropertiesException;

public interface ScheduleServiceInterface {

    void deleteNotVerifiedAccount() throws ScheduleBadPropertiesException;
    void resendConfirmationEmail();
    void unblockAccount() throws ScheduleBadPropertiesException;
}
