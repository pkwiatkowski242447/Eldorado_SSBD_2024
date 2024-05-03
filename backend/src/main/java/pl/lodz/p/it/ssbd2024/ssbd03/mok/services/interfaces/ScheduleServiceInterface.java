package pl.lodz.p.it.ssbd2024.ssbd03.mok.services.interfaces;

import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule.ScheduleBadProperties;

public interface ScheduleServiceInterface {

    void deleteNotVerifiedAccount() throws ScheduleBadProperties;
    void resendConfirmationEmail();
    void unblockAccount() throws ScheduleBadProperties;
}
