package cz.cvut.fel.rsp.ReservationSystem.rest.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.EventDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SlotDTO;

import java.time.LocalDate;
import java.util.List;

public interface EventsController {
    // GET /events/{event_id}
    public EventDTO getById(Integer eventId);

    // GET /events/{event_id}/slots?from=""&to=""
    public List<SlotDTO> getTimeSlots(Integer eventId, LocalDate fromTimestamp, LocalDate toTimestamp);

    // GET /events/{event_id}/reservations?from=""&to""
    public List<ReservationDTO> getReservations(Integer eventId, LocalDate fromTimestamp, LocalDate toTimestamp, boolean canceled);
}
