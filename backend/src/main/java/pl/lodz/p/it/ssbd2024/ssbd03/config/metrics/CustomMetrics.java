package pl.lodz.p.it.ssbd2024.ssbd03.config.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd03.aspects.logging.LoggerInterceptor;
import pl.lodz.p.it.ssbd2024.ssbd03.exceptions.ApplicationBaseException;

import java.util.concurrent.Callable;

@Component
@Getter @Setter
@LoggerInterceptor
public class CustomMetrics {

    private MeterRegistry registry;

    private Counter loginUsingCredentialsRequestCounter;
    private Counter loginUsingAuthCodeRequestCounter;
    private Counter logoutRequestCounter;

    private Timer loginUsingCredentialsRequestTimer;
    private Timer loginUsingAuthCodeRequestTimer;
    private Timer logoutRequestTimer;

    @Autowired
    public CustomMetrics(MeterRegistry meterRegistry) {
        this.loginUsingCredentialsRequestCounter = Counter.builder("authentication.controller.login.using.credentials.count")
                .description("The number of calls to authenticate with the use of basic credentials")
                .register(meterRegistry);

        this.loginUsingAuthCodeRequestCounter = Counter.builder("authentication.controller.login.using.auth.code.count")
                .description("The number of calls to authenticate with the use of auth code")
                .register(meterRegistry);

        this.logoutRequestCounter = Counter.builder("authentication.controller.logout.count")
                .description("The number of calls to authenticate with the use of auth code")
                .register(meterRegistry);

        this.loginUsingCredentialsRequestTimer = Timer.builder("authentication.controller.login.using.credentials.time")
                .description("Time taken to login using basic credentials")
                .register(meterRegistry);

        this.loginUsingAuthCodeRequestTimer = Timer.builder("authentication.controller.login.using.auth.code.time")
                .description("Time taken to finish authentication process by passing auth code")
                .register(meterRegistry);

        this.logoutRequestTimer = Timer.builder("authentication.controller.logout.time")
                .description("Time taken to finish authentication process by passing auth code")
                .register(meterRegistry);
    }

    public void recordLoginUsingCredentialsRequest() {
        loginUsingCredentialsRequestCounter.increment();
    }

    public void recordLoginUsingAuthCodeRequest() {
        loginUsingAuthCodeRequestCounter.increment();
    }

    public void recordLogoutRequest() {
        logoutRequestCounter.increment();
    }

    public <T> T timeLoginUsingCredentialsRequest(Callable<T> operation) throws ApplicationBaseException {
        try {
            return loginUsingCredentialsRequestTimer.recordCallable(operation);
        } catch (Exception exception) {
            throw new ApplicationBaseException(exception);
        }
    }

    public <T> T timeLoginUsingAuthCodeRequest(Callable<T> operation) throws ApplicationBaseException {
        try {
            return loginUsingAuthCodeRequestTimer.recordCallable(operation);
        } catch (Exception exception) {
            throw new ApplicationBaseException();
        }
    }

    public <T> T timeLogoutRequest(Callable<T> operation) throws ApplicationBaseException {
        try {
            return logoutRequestTimer.recordCallable(operation);
        } catch (Exception exception) {
            throw new ApplicationBaseException();
        }
    }
}
