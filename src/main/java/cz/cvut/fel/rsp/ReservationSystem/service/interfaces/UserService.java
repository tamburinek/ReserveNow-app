package cz.cvut.fel.rsp.ReservationSystem.service.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.user.PaymentDetails;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService{
    public void createUser(User user);

    public void addPaymentDetails(User user, PaymentDetails paymentDetails);

    public void removePaymentDetails(User user);

    User findByUsername(String username);

    public User findById(Integer id);

    public List<Reservation> findUpcomingReservations(User user);

    public List<Reservation> findPastReservations(User user);

    public List<Reservation> findUnpaidReservations(User user);

    public List<Reservation> findAllReservations(User user);

    public List<Reservation> findAllReservationInInterval(User user, LocalDate from, LocalDate to);
}
