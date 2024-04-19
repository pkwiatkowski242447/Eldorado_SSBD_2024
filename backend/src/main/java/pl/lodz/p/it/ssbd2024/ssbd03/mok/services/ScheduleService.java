package pl.lodz.p.it.ssbd2024.ssbd03.mok.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd03.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.schedule.ScheduleBadProperties;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.AccountMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.utils.consts.mok.ScheduleConsts;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ScheduleService {

    private final AccountMOKFacade accountMOKFacade;

    private final TokenFacade tokenFacade;

    @Value("${scheduler.not_verified_account_delete_time}")
    private String deleteTime;

    @Autowired
    public ScheduleService(AccountMOKFacade accountMOKFacade, TokenFacade tokenFacade) {
        this.accountMOKFacade = accountMOKFacade;
        this.tokenFacade = tokenFacade;
    }

    // FIXME delay???????
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.MINUTES, initialDelay = 0L)
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteNotVerifiedAccount() throws ScheduleBadProperties {
        log.info(ScheduleConsts.INVOKING_DELETE_ACCOUNTS_MESS);

        // Find not verified accounts
        Optional<List<Account>> inactiveAccountsOpt;
        try {
            inactiveAccountsOpt =
                    accountMOKFacade.findAllAccountsMarkedForDeletion(Long.parseLong(deleteTime), TimeUnit.MINUTES);
        } catch (NumberFormatException e) {
            throw new ScheduleBadProperties(ScheduleConsts.BAD_PROP_FORMAT.formatted("not_verified_account_delete_time"));
        }

        if (inactiveAccountsOpt.isEmpty()) {
            log.info(ScheduleConsts.NO_ACCOUNTS_TO_DELETE_MESS);
            return;
        }

        List<Account> inactiveAccounts = inactiveAccountsOpt.get();

        log.info(ScheduleConsts.LIST_ACCOUNTS_TO_DELETE_IDS, inactiveAccounts.stream().map(Account::getId).toList());

        // Delete accounts and linked tokens
        inactiveAccounts.forEach(a -> {
            tokenFacade.removeByAccount(a.getId());
            accountMOKFacade.remove(a);
        });
    }
}
