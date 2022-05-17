package cz.cvut.fel.rsp.ReservationSystem.rest.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
    private static void logException(RuntimeException ex) {
        log.error("Exception caught:", ex);
    }

    private static ErrorInfo errorInfo(HttpServletRequest request, Throwable e) {
        return new ErrorInfo(e.getMessage(), request.getRequestURI());
    }

    private static ErrorInfo errorInfo(HttpServletRequest request, Throwable e, String customMessage) {
        return new ErrorInfo(customMessage + e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorInfo> resourceNotFound(HttpServletRequest request, EntityNotFoundException e) {
        // Not necessary to log NotFoundException, they may be quite frequent and do not represent an issue with the application
        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.NOT_FOUND);
    }
}
