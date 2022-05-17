package cz.cvut.fel.rsp.ReservationSystem.model.payment;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter @NoArgsConstructor
public class Wire extends Payment{
    @NotNull
    private String foo;
}
