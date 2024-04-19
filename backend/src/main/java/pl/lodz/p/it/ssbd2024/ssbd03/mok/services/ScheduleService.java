package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ScheduleService {

    private final AccountMOKFacade accountMOKFacade;

    @Autowired
    public ScheduleService(AccountMOKFacade accountMOKFacade) {
        this.accountMOKFacade = accountMOKFacade;
    }

    //TODO delay???????
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.MINUTES, initialDelay = 1L)
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteNotVerifiedAccount() {
        log.info("Deleting unverified scheduler invoked");

        // Find not verified accounts
        var inactiveAccountsOpt = accountMOKFacade.findAllAccountsMarkedForDeletion(1L, TimeUnit.MILLISECONDS);

        if (inactiveAccountsOpt.isEmpty()) {
            log.info("No accounts");
            return;
        }

        List<Account> inactiveAccounts = inactiveAccountsOpt.get();

        log.info("Accounts to delete: {}", inactiveAccounts);

        // Delete accounts

        inactiveAccounts.forEach(accountMOKFacade::remove);
    }

}
