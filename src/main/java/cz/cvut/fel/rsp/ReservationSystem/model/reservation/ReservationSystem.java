package cz.cvut.fel.rsp.ReservationSystem.model.reservation;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.model.AbstractEntity;
import cz.cvut.fel.rsp.ReservationSystem.model.Feedback;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationSystemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class ReservationSystem extends AbstractEntity {

    @NotNull
    private String name;

    @ManyToMany
    private List<User> managers;

    @OneToMany
    private List<Feedback> feedback;

    @Override
    public String toString() {
        return "ReservationSystem{" +
                "name='" + name + '\'' +
                ", managers=" + managers +
                ", feedback=" + feedback +
                '}';
    }
}
