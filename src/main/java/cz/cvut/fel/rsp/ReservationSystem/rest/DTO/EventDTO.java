package cz.cvut.fel.rsp.ReservationSystem.rest.DTO;

import cz.cvut.fel.rsp.ReservationSystem.model.enums.Repetition;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.CustomTimeEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.IntervalEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.SeatEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class EventDTO {

    private Integer id;
    private String name;

    private LocalTime fromTime;

    private LocalTime toTime;

    private LocalDate startDate;

    private LocalDate repeatUntil;

    private DayOfWeek day;

    private Repetition repetition;

    private Integer categoryId;

    // varibles below might be null, because Event is an abstract class
    private Duration minimalReservationTime;

    private Duration intervalDuration;

    private Duration timeBetweenIntervals;

    private Integer seatAmount;

    public EventDTO(SeatEvent event) {
        this.name = event.getName();
        this.fromTime = event.getFromTime();
        this.toTime = event.getToTime();
        this.startDate = event.getStartDate();
        this.repeatUntil = event.getRepeatUntil();
        this.repetition = event.getRepetition();
        this.day = event.getDay();
        this.categoryId = event.getCategory().getId();

        this.seatAmount = event.getSeatAmount();
    }

    public EventDTO(CustomTimeEvent event) {
        this.name = event.getName();
        this.fromTime = event.getFromTime();
        this.toTime = event.getToTime();
        this.startDate = event.getStartDate();
        this.repeatUntil = event.getRepeatUntil();
        this.repetition = event.getRepetition();
        this.day = event.getDay();
        this.categoryId = event.getCategory().getId();

        this.minimalReservationTime = event.getMinimalReservationTime();
    }

    public EventDTO(IntervalEvent event) {
        this.name = event.getName();
        this.fromTime = event.getFromTime();
        this.toTime = event.getToTime();
        this.startDate = event.getStartDate();
        this.repeatUntil = event.getRepeatUntil();
        this.repetition = event.getRepetition();
        this.day = event.getDay();
        this.categoryId = event.getCategory().getId();

        this.timeBetweenIntervals = event.getTimeBetweenIntervals();
        this.intervalDuration = event.getIntervalDuration();
    }

    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.fromTime = event.getFromTime();
        this.toTime = event.getToTime();
        this.startDate = event.getStartDate();
        this.repeatUntil = event.getRepeatUntil();
        this.repetition = event.getRepetition();
        this.day = event.getDay();
        this.categoryId = event.getCategory().getId();


        if (event instanceof CustomTimeEvent) {
            this.minimalReservationTime = ((CustomTimeEvent) event).getMinimalReservationTime();
        } else if (event instanceof IntervalEvent) {
            IntervalEvent interval = ((IntervalEvent) event);
            this.timeBetweenIntervals = interval.getTimeBetweenIntervals();
            this.intervalDuration = interval.getIntervalDuration();
        } else if (event instanceof SeatEvent)
            this.seatAmount = ((SeatEvent) event).getSeatAmount();
    }
}
