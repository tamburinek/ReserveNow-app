package cz.cvut.fel.rsp.ReservationSystem.service.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.PaymentDetailsRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationSystemRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.UserRepository;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.user.PaymentDetails;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PaymentDetailsRepository paymentDetailsRepository;

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final ReservationSystemRepository reservationSystemRepository;

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void addPaymentDetails(User user, PaymentDetails paymentDetails) {
        paymentDetails.setUser(user);
        user.setPaymentDetails(paymentDetails);
        userRepository.save(user);
        paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    @Transactional
    public void removePaymentDetails(User user) {
        PaymentDetails details = user.getPaymentDetails();
        details.setUser(null);
        user.setPaymentDetails(null);
        paymentDetailsRepository.save(details);
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public List<Reservation> findUpcomingReservations(User user) {
        List<Reservation> reservations = reservationRepository.findAllUsersReservations(user);
        List<Reservation> result = new ArrayList<>();
        LocalDate thisDate = LocalDate.now();
        for (Reservation reservation : reservations){
            if (thisDate.isBefore(reservation.getReservationSlot().getDate())){
                result.add(reservation);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public List<Reservation> findPastReservations(User user) {
        List<Reservation> reservations = reservationRepository.findAllUsersReservations(user);
        List<Reservation> result = new ArrayList<>();
        LocalDate thisDate = LocalDate.now();
        for (Reservation reservation : reservations) {
            if (thisDate.isAfter(reservation.getReservationSlot().getDate())){
                result.add(reservation);
            }
        }

        return result;
    }

    @Override
    @Transactional
    public List<Reservation> findUnpaidReservations(User user) {
        List<Reservation> reservations = reservationRepository.findAllUsersReservations(user);
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation: reservations) {
            if (reservation.getPayment() == null){
                result.add(reservation);
            }
        }
        return result;
    }

    @Transactional
    public ReservationSystem findMyReservationSystem(User user){
        return reservationSystemRepository.getReservationSystemByManagersContaining(user);
    }

    @Override
    @Transactional
    public List<Reservation> findAllReservations(User user){
        return reservationRepository.findAllUsersReservations(user);
    }

    @Override
    @Transactional
    public List<Reservation> findAllReservationInInterval(User user, LocalDate from, LocalDate to){
        List<Reservation> reservations = reservationRepository.findAllUsersReservations(user);
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : reservations){
            if (reservation.getReservationSlot().getDate().isAfter(from) && reservation.getReservationSlot().getDate().isBefore(to)){
                result.add(reservation);
            }
        }
        return result;
    }

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }
}
