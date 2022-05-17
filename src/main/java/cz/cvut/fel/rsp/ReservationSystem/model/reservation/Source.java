package cz.cvut.fel.rsp.ReservationSystem.model.reservation;

import com.sun.istack.NotNull;
import cz.cvut.fel.rsp.ReservationSystem.model.AbstractEntity;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SourceDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Source extends AbstractEntity {
    @NotNull
    private String name;

    private String description;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id")
    private Address address; // Might be null, in case the source is for example an online service

    @NotNull
    private boolean isActive;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reservationSystem_id")
    private ReservationSystem reservationSystem;

    @NotNull
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    public Source(SourceDTO sourceDTO) {
        this.name = sourceDTO.getName();
        this.description = sourceDTO.getDescription();
        ;
        this.address = sourceDTO.getAddress();
        this.isActive = sourceDTO.isActive();
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    @Override
    public String toString() {
        return "Source{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address=" + address +
                ", isActive=" + isActive +
                ", reservationSystem=" + reservationSystem.getName() +
                '}';
    }
}
