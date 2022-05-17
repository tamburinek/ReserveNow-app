package cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SlotDTO;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "intervalSlot") // Interval is reserved in H2
public class Interval extends ReservationSlot{

    @NotNull
    @Column(name = "start_time")
    private LocalTime start;

    @NotNull
    @Column(name = "end_time")
    private LocalTime end;

    @Override
    public void visit(ReservationService reservationService) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String toString() {
        return "Interval{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public SlotDTO toDTO() {
        return new SlotDTO(this);
    }
}
