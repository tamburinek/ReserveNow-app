package cz.cvut.fel.rsp.ReservationSystem.dao.service;

import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationSlotRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.UserRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.testutil.Generator;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Address;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.SystemInitializerImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationServiceImplTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private ReservationSystemService reservationSystemService;

    @Autowired
    private ReservationSlotRepository slotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemInitializerImpl systemInitializer;

    private ReservationSlot slot;
    private User user;

    @BeforeEach
    public void init(){
        slot = Generator.generateReservationSlotSeat();
        slotRepository.save(slot);
        user = Generator.generateRegularUser();
        userRepository.save(user);
    }

    @Test
    public void createReservation_correctReservationCreated_reservationCreated(){
        reservationService.createReservation(user, slot);
        List<Reservation> reservations = reservationService.findAllReservations(user);

        Assertions.assertEquals(1, reservations.size());

        Reservation result = reservations.get(0);

        Assertions.assertEquals(user, result.getUser());
        Assertions.assertEquals(slot, result.getReservationSlot());
    }

    @Test
    public void createReservation_ReservationWithoutUser_exceptionThrow(){
        Assertions.assertThrows(ReservationSystemException.class,
                () -> reservationService.createReservation(null, slot));
    }

    @Test
    public void createReservation_ReservationWithoutSlot_exceptionThrow(){
        Assertions.assertThrows(ReservationSystemException.class,
                () -> reservationService.createReservation(user, null));
    }

    @Test
    public void cancelReservation_cancelReservation_reservationIsCanceled(){
        reservationService.createReservation(user, slot);
        List<Reservation> reservations = reservationService.findAllReservations(user);

        Assertions.assertEquals(1, reservations.size());

        Reservation result = reservations.get(0);
        reservationService.cancelReservation(result);
        reservations = reservationService.findAllReservations(user);

        Assertions.assertTrue(reservations.get(0).isCancelled());
    }

    @Test
    public void createReservation_createReservationAfterCancelReservation_exceptionThrow(){
        reservationService.createReservation(user, slot);
        List<Reservation> reservations = reservationService.findAllReservations(user);
        Assertions.assertEquals(1, reservations.size());

        Reservation result = reservations.get(0);
        reservationService.cancelReservation(result);
        reservations = reservationService.findAllReservations(user);
        Assertions.assertTrue(reservations.get(0).isCancelled());

        User newUser = Generator.generateRegularUser();
        userRepository.save(newUser);

        reservationService.createReservation(newUser, slot);
        reservations = reservationService.findAllReservations(newUser);
        Assertions.assertEquals(1, reservations.size());

        result = reservations.get(0);
        Assertions.assertEquals(newUser, result.getUser());
        Assertions.assertEquals(slot, result.getReservationSlot());
    }

    @Test
    public void createReservation_createReservationAfterReservationIsCreated_exceptionThrow(){
        reservationService.createReservation(user, slot);
        User newUser = Generator.generateRegularUser();
        userRepository.save(newUser);

        // melo by dojit k chybe dany slot je obsazen, ale v poradku se prepise
        Assertions.assertThrows(ReservationSystemException.class,
                () -> reservationService.createReservation(newUser, slot));
    }

    @Test
    public void findAllReservations_findAllUserReservation_allUserReservationReturned(){
        reservationService.createReservation(user, slot);
        ReservationSlot slot2 = Generator.generateReservationSlotSeat();
        slotRepository.save(slot2);
        reservationService.createReservation(user, slot2);
        ReservationSlot slot3 = Generator.generateReservationSlotSeat();
        slotRepository.save(slot3);
        reservationService.createReservation(user, slot3);
        reservationService.cancelReservation(userService.findAllReservations(user).get(0));

        int count = reservationService.findAllReservations(user).size();
        Assertions.assertEquals(3, count);
    }

    @Test
    public void findAllUnpaidReservations_findUnpaidReservation_findOnlyUnpaidReservation(){
        //TODO
    }
}
