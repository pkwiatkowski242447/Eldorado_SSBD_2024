//package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.implementations;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
//import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.TxTracked;
//import pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces.ScheduleMOPServiceInterface;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * Service managing execution of scheduled tasks.
// * Configuration concerning tasks is set in consts.properties.
// */
//@Slf4j
//@Service
//@LoggerInterceptor
//@TxTracked
//@Transactional(propagation = Propagation.REQUIRES_NEW)
//public class ScheduleMOPService implements ScheduleMOPServiceInterface {
//    @Override
//    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS, initialDelay = -1L)
//    public void endReservation() {
//    }
//}
