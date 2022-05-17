package cz.cvut.fel.rsp.ReservationSystem.model.payment;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.model.AbstractEntity;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter @Setter
public abstract class Payment{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_participant_generator")
    @SequenceGenerator(name = "course_participant_generator",sequenceName = "course_participant_id_seq")
    private Integer id;

    @NotNull
    private Integer amount;

    @NotNull
    private LocalDateTime dateTimePaid;

    @NotNull
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
