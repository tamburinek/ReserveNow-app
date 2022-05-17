package cz.cvut.fel.rsp.ReservationSystem.model.user;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.model.AbstractEntity;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PaymentDetails extends AbstractEntity {
    @NotNull
    private String creditCardNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
