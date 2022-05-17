package cz.cvut.fel.rsp.ReservationSystem.service.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.CustomTimeEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.IntervalEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.SeatEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationSlotService{
    /**
     * Generates all time slots for an event.
     */
    public void generateTimeSlots(Event event);

    /**
     * Generates intervals on a given day.
     */
    public void generateIntervalSlots(IntervalEvent event, LocalDate date);

    /**
     * Generates seats on a given day.
     */
    public void generateSeatSlots(SeatEvent event, LocalDate date);

    /**
     * Generates customtimeslots on a given day.
     */
    public void generateCustomTimeSlots(CustomTimeEvent event, LocalDate date);

    public ReservationSlot find(Integer id);

    public List<ReservationSlot> findAll(Event event);

    public List<ReservationSlot> findAll(Event event, LocalDate from, LocalDate to);

    public List<ReservationSlot> findAllReserved(Event event);

    public List<ReservationSlot> findAllReserved(Event event, LocalDate from, LocalDate to);

    public List<ReservationSlot> findAllFree(Event event);

    public List<ReservationSlot> findAllFree(Event event, LocalDate from, LocalDate to);
}
