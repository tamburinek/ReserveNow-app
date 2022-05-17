package cz.cvut.fel.rsp.ReservationSystem.rest.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SlotDTO;
import org.springframework.http.ResponseEntity;

public interface SlotController {
    // GET /slots/{slot_id}
    public SlotDTO getSlotById(Integer reservationSlotId);

    // POST /slots/{slot_id}
    public ResponseEntity<Void> createReservation (Integer reservationSlotId, ReservationDTO reservationDTO);
}
