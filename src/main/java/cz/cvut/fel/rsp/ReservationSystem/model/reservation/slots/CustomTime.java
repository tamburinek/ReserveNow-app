package cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SlotDTO;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.Duration;
import java.time.LocalTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class CustomTime extends ReservationSlot{
    @NotNull
    @Column(name ="start_time")
    private LocalTime start;

    @NotNull
    @Column(name = "end_time")
    private LocalTime end;

    @NotNull
    private boolean mainSlot; // Determines whether this is the first slot, that will get split after a reservation is made.

    @Override
    public void visit(ReservationService reservationService) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String toString() {
        return "CustomTime{" +
                "start=" + start +
                ", end=" + end +
                ", mainSlot=" + mainSlot +
                '}';
    }

    @Override
    public SlotDTO toDTO() {
        return new SlotDTO(this);
    }
}
