package cz.cvut.fel.rsp.ReservationSystem.model.reservation.events;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.dao.CategoryRepository;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.EventDTO;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.EventService;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationSlotService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SeatEvent extends Event{
    @NotNull
    private Integer seatAmount;

    @Override
    public void visit(ReservationSlotService reservationSlotService, LocalDate date) {
        reservationSlotService.generateSeatSlots(this, date);
    }

    @Override
    public void visit(EventService eventService) {
        eventService.validateSpecificEvent(this);
    }

    public SeatEvent(EventDTO dto) {
        super(dto);
        this.setSeatAmount(dto.getSeatAmount());
    }

    @Override
    public String toString() {
        return "SeatEvent{" +
                "seatAmount=" + seatAmount +
                '}';
    }
}
