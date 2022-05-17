package cz.cvut.fel.rsp.ReservationSystem.service.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.CategoryRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.EventRepository;
import cz.cvut.fel.rsp.ReservationSystem.exception.EventException;
import cz.cvut.fel.rsp.ReservationSystem.model.enums.Repetition;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.CustomTimeEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.IntervalEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.SeatEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    private final ReservationSlotServiceImpl reservationSlotService;

    @Override
    @Transactional
    public void createEvent(Event event, Category category) {
        validateEventCreation(event);
        event.setCategory(category);

        if (Objects.isNull(category.getEvents())) {
            category.setEvents(new ArrayList<>(Collections.singletonList(event)));
        }
        else{
            category.getEvents().add(event);
        }

        eventRepository.save(event);
        categoryRepository.save(category);
        reservationSlotService.generateTimeSlots(event);
    }

    public void validateSpecificEvent(IntervalEvent intervalEvent){
        // TODO
    }

    public void validateSpecificEvent(CustomTimeEvent customTimeEvent){
        // TODO
    }

    public void validateSpecificEvent(SeatEvent seatEvent){
        if (seatEvent.getSeatAmount() <= 0){
            throw new EventException("Seat amount cannot be negative or zero");
        }
    }

    private void validateEventCreation(Event event){
        if (!event.getRepetition().equals(Repetition.NONE)){
            if (Objects.isNull(event.getRepeatUntil())){
                throw new EventException("The ending date has to be specified, if the event repeats.");
            }

            if (event.getStartDate().isAfter(event.getRepeatUntil())){
                throw new EventException("The starting date is after the day the event ends");
            }
        }

        if (event.getFromTime().isAfter(event.getToTime())){
            throw new EventException("The starting time of the event is after the ending time. Start time: "
                    + event.getFromTime().toString() + " End time: " + event.getToTime().toString());
        }

        if (event.getName().length() == 0){
            throw new EventException("The event name cannot be an empty string");
        }

        event.visit(this);
    }

    @Override
    public void remove(Event event) {
    }

    @Override
    public void update(Event event) {
    }

    public Event find(Integer id) {
        return eventRepository.getById(id);
    }

    @Override
    public List<Event> getAllEvents(Source source) {
        List<Category> categories = source.getCategories();
        List<Event> result = new ArrayList<>();

        for (Category category : categories){
            List<Event> events = category.getEvents();
            for (Event event : events){
                if (!result.contains(event)){
                    result.add(event);
                }
            }
        }

        return result;
    }

    public List<ReservationSlot> findAllEventReservationSlotsInInterval(Event event, LocalDate from, LocalDate to) {
        List<ReservationSlot> reservationSlots = eventRepository.findAllReservationSlotsAtEvent(event);

        return reservationSlots.stream().filter(
                s -> (s.getDate().isEqual(from) || s.getDate().isAfter(from.minusDays(1)))
                && (s.getDate().isEqual(to) || s.getDate().isBefore(to.plusDays(1))))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findAllReservationsAtEventInInterval(Event event, LocalDate from, LocalDate to, boolean canceled) {
        List<Reservation> reservations = eventRepository.findAllReservationsAtEvent(event);

        return reservations.stream().filter(
                r -> (r.getReservationSlot().getDate().isEqual(from) || r.getReservationSlot().getDate().isAfter(from.minusDays(1))
                && (r.getReservationSlot().getDate().isEqual(to) || r.getReservationSlot().getDate().isBefore(to.plusDays(1))
                && r.isCancelled() == canceled))).collect(Collectors.toList());
    }

    public List<Event> findAllEvents(ReservationSystem reservationSystem) {
        return eventRepository.findAllEventsInReservationSystem(reservationSystem.getId());
    }

    public List<Event> findAllEvents(ReservationSystem reservationSystem, LocalDate fromDate, LocalDate toDate) {
        return eventRepository.findAllEventsInReservationSystem(reservationSystem.getId())
                .stream()
                .filter(event -> ((event.getStartDate().isEqual(fromDate) || event.getStartDate().isAfter(fromDate)) && (event.getStartDate().isEqual(toDate) || event.getStartDate().isBefore(toDate))))
                .collect(Collectors.toList());
    }

    public List<Event> findAllEventsToFuture(ReservationSystem reservationSystem) {
        return eventRepository.findAllEventsInReservationSystem(reservationSystem.getId()).stream().filter(Event -> Event.getStartDate().isAfter(LocalDate.now().minusDays(1))).collect(Collectors.toList());
    }

    @Override
    public List<Event> getEventsByCategoryName(String categoryName) {
        return eventRepository.findAllEventsByCategoryName(categoryName);
    }
}
