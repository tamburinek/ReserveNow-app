package cz.cvut.fel.rsp.ReservationSystem.rest.impl;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.EventDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SlotDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.interfaces.EventsController;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/")
@Slf4j
@RequiredArgsConstructor
public class EventsControllerImpl implements EventsController {

    private final EventService eventService;

    @Override
    @GetMapping(value = "/events/{event_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EventDTO getById(@PathVariable Integer event_id) {
        return new EventDTO(eventService.find(event_id));
    }

    @Override
    @GetMapping(value = "/events/{event_id}/slots")
    public List<SlotDTO> getTimeSlots(@PathVariable Integer event_id, @RequestParam(name = "fromTimestamp") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                      @RequestParam(name = "toTimestamp") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        Event event = eventService.find(event_id);
        List<ReservationSlot> reservationSlots = eventService.findAllEventReservationSlotsInInterval(event, fromDate, toDate);
        return reservationSlots.stream()
                .map(ReservationSlot::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping(value = "/events/{event_id}/reservations")
    public List<ReservationDTO> getReservations(@PathVariable Integer event_id, @RequestParam LocalDate fromTimestamp, @RequestParam LocalDate toTimestamp, @RequestParam boolean canceled) {
        Event event = eventService.find(event_id);
        List<Reservation> reservations = eventService.findAllReservationsAtEventInInterval(event, fromTimestamp, toTimestamp, canceled);
        return reservations.stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());
    }
}
