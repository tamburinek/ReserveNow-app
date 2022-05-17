package cz.cvut.fel.rsp.ReservationSystem.rest.DTO;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Address;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class SourceDTO {

    private String name;

    private String description;

    private Address address;

    private boolean isActive;

    private Integer reservationSystemId;

    private List<Integer> categoriesIds;

    public SourceDTO(Source source) {
        this.name = source.getName();
        this.description = source.getDescription();
        this.address = source.getAddress();
        this.isActive = source.isActive();
        this.reservationSystemId = source.getReservationSystem().getId();
        this.categoriesIds = source.getCategories().stream()
                .map(c -> (c.getId()))
                .collect(Collectors.toList());
    }
}
