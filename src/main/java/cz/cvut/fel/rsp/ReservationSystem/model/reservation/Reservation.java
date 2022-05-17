package cz.cvut.fel.rsp.ReservationSystem.model.reservation;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.model.AbstractEntity;
import cz.cvut.fel.rsp.ReservationSystem.model.payment.Payment;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Reservation extends AbstractEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "reservation")
    private Payment payment; // null if not paid

    @NotNull
    @OneToOne
    @JoinColumn(name = "reservationslot_id")
    private ReservationSlot reservationSlot;

    @NotNull
    private boolean cancelled = false;

    private String additionalInfo;

    public Reservation(ReservationDTO reservation, User user) {
        this.cancelled = reservation.isCancelled();
        this.additionalInfo = reservation.getAdditionalInfo();
        this.user = user;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "user=" + user +
                ", payment=" + payment +
                ", reservationSlot=" + reservationSlot +
                ", cancelled=" + cancelled +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
