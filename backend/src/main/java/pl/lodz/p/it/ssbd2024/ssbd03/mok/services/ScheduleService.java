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
import java.util.concurrent.TimeUnit;

/**
 * Service managing execution of scheduled tasks.
 * Configuration concerning tasks is set in application.properties.
 */
@Service
@Slf4j
public class ScheduleService {

    private final AccountMOKFacade accountMOKFacade;

    private final TokenFacade tokenFacade;

    @Value("${scheduler.not_verified_account_delete_time}")
    private String deleteTime;

    /**
     * Autowired constructor for the service.
     *
     * @param accountMOKFacade
     * @param tokenFacade
     */
    @Autowired
    public ScheduleService(AccountMOKFacade accountMOKFacade, TokenFacade tokenFacade) {
        this.accountMOKFacade = accountMOKFacade;
        this.tokenFacade = tokenFacade;
    }

    /**
     * Removes Accounts which have not finished registration.
     * Time for the Account verification is set by <code>scheduler.not_verified_account_delete_time</code> property.
     *
     * @throws ScheduleBadProperties Threw when problem with properties occurs.
     */
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteNotVerifiedAccount() throws ScheduleBadProperties {
        log.info(ScheduleConsts.INVOKING_DELETE_ACCOUNTS_MESS);

        // Find not verified accounts
        List<Account> inactiveAccounts;
        try {
            inactiveAccounts =
                    accountMOKFacade.findAllAccountsMarkedForDeletion(Long.parseLong(deleteTime), TimeUnit.HOURS);
        } catch (NumberFormatException e) {
            log.error(ScheduleConsts.BAD_PROP_FORMAT.formatted("not_verified_account_delete_time"));
            throw new ScheduleBadProperties(ScheduleConsts.BAD_PROP_FORMAT.formatted("not_verified_account_delete_time"));
        }

        if (inactiveAccounts.isEmpty()) {
            log.info(ScheduleConsts.NO_ACCOUNTS_TO_DELETE_MESS);
            return;
        }

        log.info(ScheduleConsts.LIST_ACCOUNTS_TO_DELETE_IDS, inactiveAccounts.stream().map(Account::getId).toList());

        // Delete accounts and linked tokens
        inactiveAccounts.forEach(a -> {
            tokenFacade.removeByAccount(a.getId());
            accountMOKFacade.remove(a);
        });
    }
}
