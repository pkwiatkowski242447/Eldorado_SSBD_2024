package pl.lodz.p.it.ssbd2024.ssbd03.mop.services.interfaces;

/**
 * Interface used for managing execution of scheduled tasks on behalf of module MOP.
 */
public interface ScheduleMOPServiceInterface {

    /***
     *This method is used to terminate reservation when the time of reservation has run out.
     */
    void terminateReservation();

    /**
     *This method is used to complete reservation automatically
     */
    void completeReservation();
}
