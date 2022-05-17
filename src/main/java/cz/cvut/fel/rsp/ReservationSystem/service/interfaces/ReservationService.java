package cz.cvut.fel.rsp.ReservationSystem.service.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    void createReservation(User user, ReservationSlot reservationSlot);

    void createReservation(Reservation reservation);

    void cancelReservation(Reservation reservation);

    List<Reservation> findAllReservations(User user);

    List<Reservation> findMyAllReservations(User user);

    public List<Reservation> findAllReservations(User user, LocalDate fromDate, LocalDate toDate);

    List<Reservation> findAllUnpaidReservations(User user);

    List<Reservation> findAllReservations(ReservationSystem reservationSystem);

    List<Reservation> findAllReservations(ReservationSystem reservationSystem, LocalDate from, LocalDate to);
}
