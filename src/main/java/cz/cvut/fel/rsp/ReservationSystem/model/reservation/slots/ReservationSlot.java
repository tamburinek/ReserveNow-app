package cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SlotDTO;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationService;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * ALl reservationSlots are going to be generated in the DB after creating an Event.
 *
 * We are going to know,if they are booked based on the existence of the Reservation entity bound to them.
 *
 * In case of CustomTime, one big CustomTime entity is going to be generated covering the whole interval.
 * After someone makes a reservation the entity is going to get split in two. Where the booked customTime is going to have
 * a reservation. The remaining one is going to get split again, after someone makes another reservation.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter @Setter
public abstract class ReservationSlot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_slot_generator")
    @SequenceGenerator(name = "reservation_slot_generator",sequenceName = "reservation_slot_id_seq")
    private Integer id;

    @NotNull
    private Integer price;

    @NotNull
    @Column(name = "slot_date")
    private LocalDate date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public abstract void visit(ReservationService reservationService);

    @Override
    public String toString() {
        return "ReservationSlot{" +
                "id=" + id +
                ", price=" + price +
                ", date=" + date +
                ", event=" + event +
                '}';
    }

    public abstract SlotDTO toDTO();
}
