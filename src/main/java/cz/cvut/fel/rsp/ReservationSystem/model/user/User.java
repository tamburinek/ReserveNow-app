package cz.cvut.fel.rsp.ReservationSystem.model.user;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.model.AbstractEntity;
import cz.cvut.fel.rsp.ReservationSystem.model.enums.UserType;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.UserDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "reservation_system_user")
@Getter @Setter @NoArgsConstructor
public class User extends AbstractEntity {

    public User(UserDTO userDTO) {
        this.username = userDTO.getUsername();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.userType = userDTO.getUserType();
    }

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserType userType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PaymentDetails paymentDetails;

    @ManyToMany
    private List<ReservationSystem> manages;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userType=" + userType +
                ", paymentDetails=" + paymentDetails +
                '}';
    }
}
