package pl.lodz.p.it.ssbd2024.ssbd03.actuator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.ssbd2024.ssbd03.actuator.facade.ActuatorAuthFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.actuator.facade.ActuatorMOKFacade;
import pl.lodz.p.it.ssbd2024.ssbd03.actuator.facade.ActuatorMOPFacade;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActuatorService {

    private final ActuatorMOKFacade actuatorMOKFacade;
    private final ActuatorMOPFacade actuatorMOPFacade;
    private final ActuatorAuthFacade actuatorAuthFacade;

    public boolean checkConnection() {
        return actuatorMOKFacade.checkConnection()
                && actuatorMOPFacade.checkConnection()
                && actuatorAuthFacade.checkConnection();
    }
}
