package cz.cvut.fel.rsp.ReservationSystem.rest.DTO;

import cz.cvut.fel.rsp.ReservationSystem.model.AbstractEntity;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoryDTO {

    private String name;

    private List<Integer> sourcesIds;

    private List<Integer> eventsIds;

    public CategoryDTO(Category category) {
        this.name = category.getName();
        sourcesIds = category.getSources().stream().map(AbstractEntity::getId).collect(Collectors.toList());
        eventsIds = category.getEvents().stream().map(Event::getId).collect(Collectors.toList());
    }
}
