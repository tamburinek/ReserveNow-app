package cz.cvut.fel.rsp.ReservationSystem.rest.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.SourceRepository;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.CustomTimeEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.IntervalEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.SeatEvent;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.CategoryDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.EventDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.interfaces.CategoriesController;
import cz.cvut.fel.rsp.ReservationSystem.rest.util.RestUtil;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.CategoryServiceImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.EventServiceImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.SourceServiceImpl;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1")
@Slf4j
@RequiredArgsConstructor
public class CategoriesControllerImpl implements CategoriesController {

    private final CategoryServiceImpl categoryService;
    private final SourceServiceImpl sourceService;
    private final SourceRepository sourceRepository;

    private final EventServiceImpl eventService;

    @Override
    @GetMapping(value = "/categories/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDTO getById(@PathVariable Integer categoryId) {
        return new CategoryDTO(categoryService.find(categoryId));
    }

    @GetMapping(value = "/category/{categoryId}")
    public int getSources(@PathVariable Integer categoryId) {
        for (Source source : categoryService.find(categoryId).getSources()) {
            return (source.getId());
        }
        return 0;
    }

    @GetMapping(value = "/category/{categoryId}/system")
    public int getSystemByCategory(@PathVariable Integer categoryId) {
        for (Source source : categoryService.find(categoryId).getSources()) {
            return (source.getReservationSystem().getId());
        }
        return 0;
    }

    @Override
    @GetMapping(value = "/categories/{categoryName}/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventDTO> getEventsByCategoryName(@PathVariable String categoryName) {
        return eventService.getEventsByCategoryName(categoryName).stream()
                .map(EventDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping(value = "/categories/{sourceId}/events")
    public ResponseEntity<Void> createEvent(@PathVariable Integer sourceId, @RequestBody EventDTO eventDTO) {
        Category category = new Category();
        Source source = sourceService.find(sourceId);
        category.setName("Other");
        category.addSource(source);
        Event event;
        if (eventDTO.getSeatAmount() != null)
            event = new SeatEvent(eventDTO);
        else if (eventDTO.getMinimalReservationTime() != null)
            event = new CustomTimeEvent(eventDTO);
        else
            event = new IntervalEvent(eventDTO);
        eventService.createEvent(event, category);
        source.addCategory(category);
        sourceRepository.save(source);
        log.info("Created Event {} for category with id {}", event, category.getId());
        final HttpHeaders headers = RestUtil.createLocationHeaderNewUri("events/{eventId}", event.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
