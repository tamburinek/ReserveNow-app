package cz.cvut.fel.rsp.ReservationSystem.dao.service;

import cz.cvut.fel.rsp.ReservationSystem.dao.CategoryRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.FeedbackRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationSystemRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.testutil.Generator;
import cz.cvut.fel.rsp.ReservationSystem.exception.FeedbackException;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.Feedback;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.ReservationSystemServiceImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.FeedbackService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class FeedbackServiceImplTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ReservationSystemServiceImpl reservationSystemService;

    @Autowired
    private ReservationSystemRepository reservationSystemRepository;

    private ReservationSystem reservationSystem;

    @BeforeEach
    public void init() {
        User owner = Generator.generateSystemOwner();
        reservationSystem = Generator.generateReservationSystem(new ArrayList<>(), new ArrayList<>());
        reservationSystemService.createReservationSystem(owner, reservationSystem);
    }

    @Test
    public void createFeedback_createsNewFeedback_newFeedbackisCreated(){
        Feedback feedback = new Feedback();
        String message = "testFeedback";
        feedback.setMessage(message);
        feedbackService.createFeedback(feedback, reservationSystem);

        ReservationSystem resultSystem = reservationSystemRepository.findById(reservationSystem.getId()).get();
        Feedback resultFeedback = resultSystem.getFeedback().get(0);

        Assertions.assertEquals(resultFeedback.getMessage(), message);
    }

    @Test
    public void createFeedback_createsNewFeedbackWithoutMessage_throwException(){
        Assertions.assertThrows(FeedbackException.class,
                () -> feedbackService.createFeedback(new Feedback(), reservationSystem));
    }

    @Test
    public void removeFeedback_removesCorrectFeedback_feedbackIsRemoved(){
        Feedback feedback = new Feedback();
        String message = "testFeedback";
        feedback.setMessage(message);
        feedbackService.createFeedback(feedback, reservationSystem);

        ReservationSystem resultSystem = reservationSystemRepository.findById(reservationSystem.getId()).get();
        Feedback resultFeedback = resultSystem.getFeedback().get(0);

        Assertions.assertEquals(resultFeedback.getMessage(), message);

        feedbackService.deleteFeedback(reservationSystem, resultFeedback);
        resultSystem = reservationSystemRepository.findById(reservationSystem.getId()).get();
        int resultFeedbackSize = resultSystem.getFeedback().size();

        Assertions.assertEquals(0, resultFeedbackSize);
    }

    @Test
    public void removeFeedback(){
        Feedback feedback1 = new Feedback();
        Feedback feedback2 = new Feedback();
        String message1 = "testFeedback1";
        String message2 = "testFeedback2";
        feedback1.setMessage(message1);
        feedback2.setMessage(message2);
        ReservationSystem reservationSystem2 = Generator.generateReservationSystem(new ArrayList<>(), new ArrayList<>());
        reservationSystemService.createReservationSystem(Generator.generateSystemOwner(), reservationSystem2);
        feedbackService.createFeedback(feedback1, reservationSystem);
        feedbackService.createFeedback(feedback2, reservationSystem2);

        ReservationSystem resultSystem1 = reservationSystemRepository.findById(reservationSystem.getId()).get();
        Feedback resultFeedback1 = resultSystem1.getFeedback().get(0);

        ReservationSystem resultSystem2 = reservationSystemRepository.findById(reservationSystem2.getId()).get();
        Feedback resultFeedback2 = resultSystem1.getFeedback().get(0);

        Assertions.assertEquals(resultFeedback1.getMessage(), message1);
        Assertions.assertEquals(resultFeedback2.getMessage(), message1);

        Assertions.assertThrows(FeedbackException.class,
                () -> feedbackService.deleteFeedback(reservationSystem2, resultFeedback1 ));
    }
}
