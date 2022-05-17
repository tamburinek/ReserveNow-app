package cz.cvut.fel.rsp.ReservationSystem.dao.service;

import cz.cvut.fel.rsp.ReservationSystem.dao.CategoryRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.IntervalRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.SeatRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.testutil.Generator;
import cz.cvut.fel.rsp.ReservationSystem.model.enums.Repetition;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.SeatEvent;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.ReservationSlotServiceImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.EventService;
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
public class ReservationSlotServiceImplTest {

    @Autowired
    private ReservationSlotServiceImpl reservationSlotService;

    @Autowired
    private EventService eventService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private IntervalRepository intervalRepository;

    private Category category;

    @BeforeEach
    public void init() {
        category = Generator.generateCategory();
        categoryRepository.save(category);
    }

    @Test
    void generateTimeSlots_seatEventWithoutRepetition_seatsGenerated() {
        SeatEvent seatEvent = Generator.generateSeatEventWithoutRepetition();
        seatEvent.setSeatAmount(15);

        eventService.createEvent(seatEvent, category); // <- reservationSlotService.generateTimeSlots(seatEvent) in it

        Assertions.assertEquals(15, seatRepository.findAll().size());
        Assertions.assertEquals(seatEvent.getStartDate(), seatRepository.findAll().get(0).getDate());
    }

    @Test
    void generateTimeSlots_seatEventWithDaysRepetition_seatsGenerated() {
        SeatEvent seatEvent = Generator.generateSeatEventWithoutRepetition();
        seatEvent.setRepetition(Repetition.DAILY);
        seatEvent.setStartDate(LocalDate.of(2023, 10, 10));
        seatEvent.setRepeatUntil(LocalDate.of(2023, 10, 13));
        seatEvent.setSeatAmount(5);

        eventService.createEvent(seatEvent, category);

        Assertions.assertEquals(5 * 4, seatRepository.findAll().size());

        for (int i = 0; i < 4; i++) { // control each date for each seat
            for (int j = 0; j < 5; j++) {
                Assertions.assertEquals (
                        LocalDate.of(2023, 10, 10 + i),
                        seatRepository.findAll().get(5 * i + j).getDate()
                );
            }
        }
    }

    @Test
    public void generateTimeSlots_IntervalsEventWithoutRepetition_intervalsGenerated() {
        Event event = Generator.generateIntervalEventWithoutRepetition();

        eventService.createEvent(event, category);

        Assertions.assertEquals(2, intervalRepository.findAll().size()); // not sure how it works
        Assertions.assertEquals(event.getStartDate(), intervalRepository.findAll().get(0).getDate());
    }

    @Test
    public void generateTimeSlots_IntervalsEventWithDaysRepetition_intervalsGenerated() {
        Event event = Generator.generateIntervalEventWithoutRepetition();
        event.setRepetition(Repetition.DAILY);
        event.setStartDate(LocalDate.of(2023, 10, 10));
        event.setRepeatUntil(LocalDate.of(2023, 10, 13));

        eventService.createEvent(event, category);

        Assertions.assertEquals(2 * 4, intervalRepository.findAll().size()); // not sure how it works (2 intervals?)

        for (int i = 0; i < 4; i++) { // control each date for each interval
            for (int j = 0; j < 2; j++) {
                Assertions.assertEquals (
                        LocalDate.of(2023, 10, 10 + i),
                        intervalRepository.findAll().get(2 * i + j).getDate()
                );
            }
        }
    }

    @Test
    public void findAll_seatEventsWithoutRepetition_allReservationSlotsFound(){
        SeatEvent seatEvent = Generator.generateSeatEventWithoutRepetition();
        seatEvent.setStartDate(LocalDate.of(2023, 10, 10));
        seatEvent.setRepeatUntil(LocalDate.of(2023, 10, 13));
        seatEvent.setSeatAmount(5);

        eventService.createEvent(seatEvent, category);

        Assertions.assertEquals(5, reservationSlotService.findAll(seatEvent).size());
    }

    @Test
    public void findAllFree_seatEventsWithoutRepetition_allFreeReservationSlotsFound(){
        SeatEvent seatEvent = Generator.generateSeatEventWithoutRepetition();
        seatEvent.setStartDate(LocalDate.of(2023, 10, 10));
        seatEvent.setRepeatUntil(LocalDate.of(2023, 10, 13));
        seatEvent.setSeatAmount(5);

        eventService.createEvent(seatEvent, category);

        Assertions.assertEquals(5, reservationSlotService.findAllFree(seatEvent).size());
    }

    @Test
    public void findAllFree_seatEventsWithoutRepetitionInYear2025_noReservationSlotsFound(){
        SeatEvent seatEvent = Generator.generateSeatEventWithoutRepetition();
        seatEvent.setStartDate(LocalDate.of(2023, 10, 10));
        seatEvent.setRepeatUntil(LocalDate.of(2023, 10, 13));
        seatEvent.setSeatAmount(5);
        LocalDate queryStart = LocalDate.of(2025, 10, 10);
        LocalDate queryEnd = LocalDate.of(2025, 11, 11);


        eventService.createEvent(seatEvent, category);

        Assertions.assertEquals(0, reservationSlotService.findAllFree(seatEvent, queryStart, queryEnd).size());
    }

    @Test
    public void findAllFree_seatEventsWithoutRepetitionIn2023_allReservationSlotsFound(){
        SeatEvent seatEvent = Generator.generateSeatEventWithoutRepetition();
        seatEvent.setStartDate(LocalDate.of(2023, 10, 10));
        seatEvent.setRepeatUntil(LocalDate.of(2023, 10, 13));
        seatEvent.setSeatAmount(5);
        LocalDate queryStart = LocalDate.of(2023, 10, 10);
        LocalDate queryEnd = LocalDate.of(2023, 10, 13);


        eventService.createEvent(seatEvent, category);

        Assertions.assertEquals(5, reservationSlotService.findAllFree(seatEvent, queryStart, queryEnd).size());
    }

    @Test
    public void findAllReserved_seatEventsWithoutRepetition_noReservationSlotsFound(){
        SeatEvent seatEvent = Generator.generateSeatEventWithoutRepetition();
        seatEvent.setStartDate(LocalDate.of(2023, 10, 10));
        seatEvent.setRepeatUntil(LocalDate.of(2023, 10, 13));
        seatEvent.setSeatAmount(5);

        eventService.createEvent(seatEvent, category);

        Assertions.assertEquals(0, reservationSlotService.findAllReserved(seatEvent).size());
    }
}
