package cz.cvut.fel.rsp.ReservationSystem.rest.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.PaymentDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ReservationsController {
    // GET /reservations/{reservation_id}
    public ReservationDTO getById(Integer reservationId);

    // DELETE /reservations/{reservation_id}
    public ResponseEntity<Void> cancel(Integer reservationId);

}
