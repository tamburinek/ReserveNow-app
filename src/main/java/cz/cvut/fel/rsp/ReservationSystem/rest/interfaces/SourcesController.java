package cz.cvut.fel.rsp.ReservationSystem.rest.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.AddressDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.CategoryDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.EventDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SourceDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SourcesController {
    // /sources/{sourceId}
    public SourceDTO getById(Integer sourceId);

    // /sources/{sourceId}/events
    public List<EventDTO> getEvents(Integer sourceId, Integer fromTimestamp, Integer toTimeStamp);

    // /sources/{sourceId}/categories
    public List<CategoryDTO> getCategories(Integer sourceId);

    // /sources/{sourceId}
    public ResponseEntity<Void> createCategory(Integer sourceId, CategoryDTO categoryDTO);

    // /sources/{sourceId}/address
    public AddressDTO getAddress(Integer sourceId);

    // /sources/{sourceId}/address
    public ResponseEntity<Void> createAddress(Integer sourceId, AddressDTO addressDTO);
}
