package cz.cvut.fel.rsp.ReservationSystem.model.reservation.events;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.EventDTO;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.EventService;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationSlotService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.Duration;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CustomTimeEvent extends Event{
    @NotNull
    private Duration minimalReservationTime;

    @Override
    public void visit(ReservationSlotService reservationSlotService, LocalDate date) {
        reservationSlotService.generateCustomTimeSlots(this, date);
    }

    @Override
    public void visit(EventService eventService) {
        eventService.validateSpecificEvent(this);
    }

    public CustomTimeEvent (EventDTO dto) {
        super(dto);
        this.setMinimalReservationTime(dto.getMinimalReservationTime());
    }

    @Override
    public String toString() {
        return "CustomTimeEvent{" +
                "minimalReservationTime=" + minimalReservationTime +
                '}';
    }
}
