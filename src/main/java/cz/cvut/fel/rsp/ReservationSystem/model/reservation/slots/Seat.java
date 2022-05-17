package cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SlotDTO;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter @Setter @NoArgsConstructor
public class Seat extends ReservationSlot{
    @NotNull
    private String seatIdentifier;

    @Override
    public void visit(ReservationService reservationService) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatIdentifier='" + seatIdentifier + '\'' +
                '}';
    }

    @Override
    public SlotDTO toDTO() {
        return new SlotDTO(this);
    }
}
