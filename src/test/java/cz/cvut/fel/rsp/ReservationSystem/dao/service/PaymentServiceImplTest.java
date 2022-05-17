package cz.cvut.fel.rsp.ReservationSystem.dao.service;

import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationSlotRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.UserRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.testutil.Generator;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.CategoryService;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.PaymentService;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationService;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationSystemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationSystemService reservationSystemService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationSlotRepository slotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ReservationSlot slot;
    private User user;
    private Reservation reservation;

    @BeforeEach
    public void init(){
        slot = Generator.generateReservationSlotSeat();
        slotRepository.save(slot);
        user = Generator.generateRegularUser();
        userRepository.save(user);
        reservationService.createReservation(user, slot);
        reservation = reservationService.findAllReservations(user).get(0);
    }

    @Test
    public void createCashPayment_createPayment_paymentIsCreated(){
        Reservation result = reservationRepository.findAllUsersReservations(user).get(0);
        Assertions.assertNull(result.getPayment());

        paymentService.createCashPayment(reservation);
        result = reservationRepository.findAllUsersReservations(user).get(0);

        Assertions.assertNotNull(result.getPayment());
    }

    @Test
    public void createCashPayment_createPaymentWithoutReservation_ExceptionThrow(){
        Assertions.assertThrows(ReservationSystemException.class,
                () -> paymentService.createCashPayment(null));
    }

    @Test
    public void createCashPayment_createPaymentOnCanceledReservation_ExceptionThrow(){
        reservationService.cancelReservation(reservation);
        Assertions.assertThrows(ReservationSystemException.class,
                () -> paymentService.createCashPayment(null));
    }

    @Test
    public void createWirehPayment_createPayment_paymentIsCreated(){
        Reservation result = reservationRepository.findAllUsersReservations(user).get(0);
        Assertions.assertNull(result.getPayment());

        paymentService.createCashPayment(reservation);
        result = reservationRepository.findAllUsersReservations(user).get(0);

        Assertions.assertNotNull(result.getPayment());
    }

    @Test
    public void createWirePayment_createPaymentWithoutReservation_ExceptionThrow(){
        Assertions.assertThrows(ReservationSystemException.class,
                () -> paymentService.createWirePayment(null));
    }

    @Test
    public void createWirePayment_createPaymentOnCanceledReservation_ExceptionThrow(){
        reservationService.cancelReservation(reservation);
        Assertions.assertThrows(ReservationSystemException.class,
                () -> paymentService.createWirePayment(null));
    }

}
