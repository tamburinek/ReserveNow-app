package cz.cvut.fel.rsp.ReservationSystem.rest.DTO;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDTO {
    @NotNull
    private String city;

    @NotNull
    private String street;

    @NotNull
    private String houseNumber;

    @NotNull
    private String postalCode;

    public AddressDTO(Address address) {
        this.city = address.getCity();
        this.street = address.getStreet();
        this.houseNumber = address.getHouseNumber();
        this.postalCode = address.getPostalCode();
    }
}
