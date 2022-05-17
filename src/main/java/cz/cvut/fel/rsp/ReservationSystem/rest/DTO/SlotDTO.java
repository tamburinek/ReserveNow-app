package cz.cvut.fel.rsp.ReservationSystem.rest.DTO;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class SlotDTO {

    private Integer id, price, eventId;
    private LocalDate date;

    private String type;

    // Interval type
    private LocalTime startTime;
    private LocalTime endTime;

    // Seat
    private String seatIdentifier;


    public SlotDTO(Seat seat) {
        mapCommon(seat);
        this.seatIdentifier = seat.getSeatIdentifier();
        this.startTime = seat.getEvent().getFromTime();
        this.endTime = seat.getEvent().getToTime();
    }

    public SlotDTO(Interval interval) {
        mapCommon(interval);
        this.startTime = interval.getStart();
        this.endTime = interval.getEnd();
    }

    public SlotDTO(CustomTime customTime) {
        mapCommon(customTime);
        this.startTime = customTime.getStart();
        this.endTime = customTime.getEnd();
    }

    public SlotDTO(FixedLengthCustomTime customTime) {
        mapCommon(customTime);
        // TODO not implemented in the rest of the system
    }

    private void mapCommon(ReservationSlot slot) {
        this.id = slot.getId();
        this.price = slot.getPrice();
        this.eventId = slot.getEvent().getId();
        this.date = slot.getDate();
    }
}
