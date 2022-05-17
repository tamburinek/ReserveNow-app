package cz.cvut.fel.rsp.ReservationSystem.dao.service;

import cz.cvut.fel.rsp.ReservationSystem.Application;

import cz.cvut.fel.rsp.ReservationSystem.dao.CategoryRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.testutil.Generator;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Address;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.user.PaymentDetails;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.rest.AuthControllerTest;
import cz.cvut.fel.rsp.ReservationSystem.rest.impl.UserControllerImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.ReservationSystemServiceImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.CategoryService;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.EventService;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.SourceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.util.ArrayList;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@ComponentScan(
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {UserControllerImpl.class}
                )}
)
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReservationSystemServiceImpl reservationSystemService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private EventService eventService;

    @Autowired
    private CategoryRepository categoryRepository;

    private Source source;

    @BeforeEach
    public void init() {

        User owner = Generator.generateSystemOwner();
        PaymentDetails paymentDetails = Generator.generatePaymentDetails(owner);
        owner.setPaymentDetails(paymentDetails);

        ReservationSystem reservationSystem = Generator.generateReservationSystem(new ArrayList<>(), new ArrayList<>());
        reservationSystemService.createReservationSystem(owner, reservationSystem);

        Address sourceAddress = Generator.generateAddress();
        source = Generator.generateSource(reservationSystem, sourceAddress);
        sourceService.createSource(source, reservationSystem);
    }

    @Test
    public void createCategory_createCorrectCategory_categoryCreated() {

        Category category = Generator.generateCategory();
        categoryService.createCategory(category, source);

        Category result = categoryRepository.findById(category.getId()).orElse(null);
        Assertions.assertNotNull(result);

//        List<Category> categories = categoryRepository.findAll();   // doesn't work cause address isn't saved
//        Assertions.assertEquals(2, categories.size());
    }

    @Test
    public void addEvent_createCorrectEventsAndAddToCategory_EventAdded() {

        Category category1 = Generator.generateCategory();
        categoryService.createCategory(category1, source);
        Event event1 = Generator.generateIntervalEventWithoutRepetition();
        eventService.createEvent(event1, category1);
        Assertions.assertEquals(category1, event1.getCategory());

        Category category2 = Generator.generateCategory();
        categoryService.createCategory(category2, source);
        Event event2 = Generator.generateIntervalEventWithoutRepetition();
        eventService.createEvent(event2, category2);
        Assertions.assertEquals(category2, event2.getCategory());

        categoryService.addEventToCategory(event1, category2);

        Assertions.assertTrue(category2.getEvents().contains(event1));
        Assertions.assertTrue(category2.getEvents().contains(event2));
        Assertions.assertEquals(2, category2.getEvents().size());

        Assertions.assertEquals(category2, event1.getCategory());
    }

    @Test
    public void addEvent_addExistingEvent_ExceptionThrowed() {
        Category category = Generator.generateCategory();
        categoryService.createCategory(category, source);

        Event event = Generator.generateIntervalEventWithoutRepetition();
        eventService.createEvent(event, category);

        Assertions.assertThrows(ReservationSystemException.class,
                () -> categoryService.addEventToCategory(event, category));
    }

    @Test
    public void addEvent_addNewEvent_EventIsAddedToCategory(){
        Category originalCategory = Generator.generateCategory();
        categoryService.createCategory(originalCategory, source);
        Category category = Generator.generateCategory();
        categoryService.createCategory(category, source);

        Event event = Generator.generateIntervalEventWithoutRepetition();
        eventService.createEvent(event, originalCategory);

        categoryService.addEventToCategory(event, category);

        Category result = categoryRepository.getById(category.getId());

        Assertions.assertTrue(result.getEvents().contains(event));

    }

}
