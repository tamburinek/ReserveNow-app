package cz.cvut.fel.rsp.ReservationSystem.dao.service;

import cz.cvut.fel.rsp.ReservationSystem.dao.*;
import cz.cvut.fel.rsp.ReservationSystem.dao.testutil.Generator;
import cz.cvut.fel.rsp.ReservationSystem.model.payment.Cash;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.Seat;
import cz.cvut.fel.rsp.ReservationSystem.model.user.PaymentDetails;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    CashRepository cashRepository;

    @Autowired
    ReservationRepository reservationRepository;

    private User user;

    @BeforeEach
    public void init(){
        user = Generator.generateRegularUser();
        userRepository.save(user);
    }

    @Test
    public void findUnpaidReservations_findReservations_noUnpaidReservationsFound() {

        Seat reservationSlot = Generator.generateReservationSlotSeat();
        Reservation reservation = Generator.generateReservation(reservationSlot);
        reservation.setAdditionalInfo("Test");
        Cash cash = Generator.generateCash(reservation);
        reservation.setPayment(cash);

        seatRepository.save(reservationSlot);
        cashRepository.save(cash);
        reservationRepository.save(reservation);
        userRepository.save(reservation.getUser());
        paymentDetailsRepository.save(reservation.getUser().getPaymentDetails());

        Assertions.assertEquals(0, userService.findUnpaidReservations(reservation.getUser()).size());
    }

    @Test
    public void findUnpaidReservations_findReservations_UnpaidReservationsFound() {

        Seat reservationSlot = Generator.generateReservationSlotSeat();
        Reservation reservation = Generator.generateReservation(reservationSlot);
        reservation.setAdditionalInfo("Test");

        seatRepository.save(reservationSlot);
        reservationRepository.save(reservation);
        userRepository.save(reservation.getUser());
        paymentDetailsRepository.save(reservation.getUser().getPaymentDetails());

        Assertions.assertEquals(1, userService.findUnpaidReservations(reservation.getUser()).size());
        Assertions.assertEquals(reservation, userService.findUnpaidReservations(reservation.getUser()).get(0));
    }

    @Test
    public void addPaymentDetails_addPaymentDetails_PaymentDetailsAdded(){
        PaymentDetails payment = Generator.generatePaymentDetails(user);
        paymentDetailsRepository.save(payment);

        userService.addPaymentDetails(user, payment);
        paymentDetailsRepository.findById(payment.getId());

        Assertions.assertEquals(payment, user.getPaymentDetails());
    }

    @Test
    public void removePaymentDetails_removePaymentDetails_PaymentAndUserAreNotConnected(){
        PaymentDetails payment = Generator.generatePaymentDetails(user);
        paymentDetailsRepository.save(payment);

        userService.addPaymentDetails(user, payment);
        paymentDetailsRepository.findById(payment.getId());
        Optional<User> resultUser = userRepository.findById(user.getId());

        Assertions.assertEquals(payment, resultUser.get().getPaymentDetails());

        userService.removePaymentDetails(user);

        Assertions.assertNull(resultUser.get().getPaymentDetails());
        Assertions.assertNull(payment.getUser());
    }

    @Test
    public void findUpcomingReservations_upcomingReservations_returnUpcomingReservations(){
        Seat reservationSlot = Generator.generateReservationSlotSeat();
        Reservation reservation = Generator.generateReservation(reservationSlot);
        reservationSlot.setDate(LocalDate.now().plusDays(1));

        user = reservation.getUser();

        seatRepository.save(reservationSlot);
        reservationRepository.save(reservation);
        userRepository.save(user);

        List<Reservation> reservations = userService.findUpcomingReservations(user);
        Reservation result = reservations.get(0);

        Assertions.assertEquals(1,reservations.size());
        Assertions.assertEquals(reservation, result);
    }

    @Test
    public void findUpcomingReservations_pastReservations_returnEmptyList(){
        Seat reservationSlot = Generator.generateReservationSlotSeat();
        Reservation reservation = Generator.generateReservation(reservationSlot);
        reservationSlot.setDate(LocalDate.now().minusDays(1));

        user = reservation.getUser();
        seatRepository.save(reservationSlot);
        reservationRepository.save(reservation);
        userRepository.save(user);

        List<Reservation> reservations = userService.findUpcomingReservations(user);
        Assertions.assertEquals(0,reservations.size());
    }

    @Test
    public void findPastReservations_pastReservations_returnPastReservations(){
        Seat reservationSlot = Generator.generateReservationSlotSeat();
        Reservation reservation = Generator.generateReservation(reservationSlot);
        reservationSlot.setDate(LocalDate.now().minusDays(1));

        user = reservation.getUser();

        seatRepository.save(reservationSlot);
        reservationRepository.save(reservation);
        userRepository.save(user);

        List<Reservation> reservations = userService.findPastReservations(user);
        Reservation result = reservations.get(0);

        Assertions.assertEquals(1,reservations.size());
        Assertions.assertEquals(reservation, result);
    }

    @Test
    public void findPastReservations_upcomingReservations_returnEmptyList(){
        Seat reservationSlot = Generator.generateReservationSlotSeat();
        Reservation reservation = Generator.generateReservation(reservationSlot);
        reservationSlot.setDate(LocalDate.now().plusDays(1));

        user = reservation.getUser();

        seatRepository.save(reservationSlot);
        reservationRepository.save(reservation);
        userRepository.save(user);

        List<Reservation> reservations = userService.findPastReservations(user);
        Assertions.assertEquals(0,reservations.size());
    }

    @Test
    public void findAllReservationInInterval_reservationInInterval_returnReservationsInInterval(){
        Seat reservationSlot = Generator.generateReservationSlotSeat();
        Reservation reservation = Generator.generateReservation(reservationSlot);
        reservationSlot.setDate(LocalDate.now());

        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = LocalDate.now().plusDays(1);

        user = reservation.getUser();

        seatRepository.save(reservationSlot);
        reservationRepository.save(reservation);
        userRepository.save(user);

        List<Reservation> results = userService.findAllReservationInInterval(user, from, to);
        Reservation result = results.get(0);

        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(reservation, result);
    }

    @Test
    public void findAllReservationInInterval_reservationisAfterInterval_returnEmptyList(){
        Seat reservationSlot = Generator.generateReservationSlotSeat();
        Reservation reservation = Generator.generateReservation(reservationSlot);
        reservationSlot.setDate(LocalDate.now());

        LocalDate from = LocalDate.now().minusDays(2);
        LocalDate to = LocalDate.now().minusDays(1);

        user = reservation.getUser();

        seatRepository.save(reservationSlot);
        reservationRepository.save(reservation);
        userRepository.save(user);

        List<Reservation> results = userService.findAllReservationInInterval(user, from, to);

        Assertions.assertEquals(0, results.size());
    }

    @Test
    public void findAllReservationInInterval_reservationisBeforeInterval_returnEmptyList() {
        Seat reservationSlot = Generator.generateReservationSlotSeat();
        Reservation reservation = Generator.generateReservation(reservationSlot);
        reservationSlot.setDate(LocalDate.now());

        LocalDate from = LocalDate.now().plusDays(1);
        LocalDate to = LocalDate.now().plusDays(2);

        user = reservation.getUser();

        seatRepository.save(reservationSlot);
        reservationRepository.save(reservation);
        userRepository.save(user);

        List<Reservation> results = userService.findAllReservationInInterval(user, from, to);

        Assertions.assertEquals(0, results.size());
    }
}
