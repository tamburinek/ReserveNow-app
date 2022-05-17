package cz.cvut.fel.rsp.ReservationSystem.service.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;

public interface CategoryService{
    void createCategory(Category category, Source source);

    void addEventToCategory(Event event, Category category);

    void removeEventFromCategory(Event event, Category category);

    void update(Category category);

    void remove(Category toRemove, Category moveEventsTo);

    Category find(Integer id);


}
