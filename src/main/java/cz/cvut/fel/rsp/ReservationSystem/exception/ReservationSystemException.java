package cz.cvut.fel.rsp.ReservationSystem.exception;

/**
 * Base for all application-specific exceptions.
 */
public class ReservationSystemException extends RuntimeException {

    public ReservationSystemException() {
    }

    public ReservationSystemException(String message) {
        super(message);
    }

    public ReservationSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationSystemException(Throwable cause) {
        super(cause);
    }
}
