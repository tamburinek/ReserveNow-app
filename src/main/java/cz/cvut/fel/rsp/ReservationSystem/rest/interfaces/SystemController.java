package cz.cvut.fel.rsp.ReservationSystem.rest.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.model.Feedback;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationSystemDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SourceDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface SystemController {
    // GET /systems
    public List<ReservationSystemDTO> getReservationSystems();

    // POST /systems
    public ResponseEntity<Void> createSystem(ReservationSystemDTO reservationSystemDTO);

    // GET /systems/{system_id}
    public ReservationSystemDTO getById(Integer systemId);

    // GET /systems/{system_id}/sources
    public List<SourceDTO> getSources(Integer systemId);

    // GET /systems/feedback
    public List<Feedback> getFeedback();

    // POST /systems/{system_id}/feedback
    public ResponseEntity<Void> createFeedback(Integer systemId, Feedback feedback);

    // POST /systems/{system_id}/sources
    public ResponseEntity<Void> createSource(SourceDTO sourceDTO);

    // GET /systems/{system_id}/reservations?fromDate=2022-01-01&toDate=2022-02-02
    // Kdyz budou chtit today, tak tam daji dnesni datum
    public List<ReservationDTO> getAllReservationsFromTo(Integer systemId, LocalDate fromDate, LocalDate toDate);
}
