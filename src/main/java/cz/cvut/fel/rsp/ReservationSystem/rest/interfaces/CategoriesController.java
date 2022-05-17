package cz.cvut.fel.rsp.ReservationSystem.rest.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.CategoryDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.EventDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoriesController {
    // GET /categories/{categoryId}
    public CategoryDTO getById(Integer categoryId);

    // GET /categories/{categoryName}/events
    public List<EventDTO> getEventsByCategoryName(String categoryName);

    // POST /categories/{categoryId}
    public ResponseEntity<Void> createEvent(Integer categoryId, EventDTO eventDTO);
}
