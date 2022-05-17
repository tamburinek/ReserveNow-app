package cz.cvut.fel.rsp.ReservationSystem.dao.service;

import cz.cvut.fel.rsp.ReservationSystem.dao.CategoryRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.EventRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.testutil.Generator;
import cz.cvut.fel.rsp.ReservationSystem.exception.EventException;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.SeatEvent;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class EventServiceImplTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventServiceImpl eventService;

    private Category category;

    @BeforeEach
    void init() {
        category = Generator.generateCategory();
        categoryRepository.save(category);
    }

    @Test
    void createEvent_createNoRepetitionEventWithIntervals_eventCreated() {
        Event event = Generator.generateIntervalEventWithoutRepetition();

        eventService.createEvent(event, category);

        Assertions.assertNotNull(eventRepository.findById(event.getId()).orElse(null));
    }


    @Test
    void createEvent_createInvalidRepetitionEventWithIntervals_ExceptionTrowed() {
        Event event = Generator.generateIntervalEventWithRepetition();
        event.setRepeatUntil(null);

        Assertions.assertThrows(EventException.class, () -> eventService.createEvent(event, category));
    }

    @Test
    void createEvent_createInvalidStartDayAndRepetitionEventWithIntervals_ExceptionTrowed() {
        Event event = Generator.generateIntervalEventWithRepetition();
        event.setRepeatUntil(LocalDate.of(2022, 10, 10));
        event.setStartDate(LocalDate.of(2023, 10, 10));

        Assertions.assertThrows(EventException.class, () -> eventService.createEvent(event, category));
    }

    @Test
    void createEvent_createInvalidFromAndToTimeRepetitionEventWithIntervals_ExceptionTrowed() {
        Event event = Generator.generateIntervalEventWithoutRepetition();
        event.setFromTime(LocalTime.of(4,0));
        event.setToTime(LocalTime.of(1,0));

        Assertions.assertThrows(EventException.class, () -> eventService.createEvent(event, category));
    }

    @Test
    void createEvent_createInvalidNameEventWithIntervals_ExceptionTrowed() {
        Event event = Generator.generateIntervalEventWithoutRepetition();
        event.setName("");

        Assertions.assertThrows(EventException.class, () -> eventService.createEvent(event, category));
    }

    @Test
    void createEvent_createInvalidSeatAmountEventWithSeats_ExceptionTrowed() {
        SeatEvent event = Generator.generateSeatEventWithoutRepetition();
        event.setSeatAmount(-1);

        Assertions.assertThrows(EventException.class, () -> eventService.createEvent(event,category));
    }




}
