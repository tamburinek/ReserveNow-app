package cz.cvut.fel.rsp.ReservationSystem.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Feedback extends AbstractEntity{
    @NotNull
    private String message;
}
