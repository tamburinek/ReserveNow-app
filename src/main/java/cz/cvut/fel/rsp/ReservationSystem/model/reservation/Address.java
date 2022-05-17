package cz.cvut.fel.rsp.ReservationSystem.model.reservation;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.model.AbstractEntity;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.AddressDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter @Setter @NoArgsConstructor
public class Address extends AbstractEntity {

    @NotNull
    private String city;

    @NotNull
    private String street;

    @NotNull
    private String houseNumber;

    @NotNull
    private String postalCode;

    public Address(AddressDTO addressDTO) {
        this.city = addressDTO.getCity();
        this.street = addressDTO.getStreet();
        this.houseNumber = addressDTO.getHouseNumber();
        this.postalCode = addressDTO.getHouseNumber();
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
