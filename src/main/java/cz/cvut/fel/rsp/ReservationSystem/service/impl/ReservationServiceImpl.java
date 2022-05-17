package cz.cvut.fel.rsp.ReservationSystem.service.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.*;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationException;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final EventServiceImpl eventServiceImpl;

    @Override
    public void createReservation(User user, ReservationSlot reservationSlot) {
        Reservation reservation = new Reservation();
        if (user == null) {
            throw new ReservationException("Reservation has to have its user to be created.");
        }
        if (reservationSlot == null) {
            throw new ReservationException("Slot ");
        }
        Reservation helper = reservationRepository.findNotCancelledReservationForReservationSlot(reservationSlot.getId());
        if (helper != null && !helper.isCancelled()){
            throw new ReservationSystemException("Slot already has reservation");
        }
        reservation.setUser(user);
        reservation.setReservationSlot(reservationSlot);
        reservationRepository.save(reservation);
    }

    public void createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void cancelReservation(Reservation reservation) {
        if (reservation == null) {
            throw new ReservationException("Reservation has to be mentioned to cancel.");
        }
        Reservation tmp = reservationRepository.findReservationById(reservation.getId());
        tmp.setCancelled(true);
        reservationRepository.save(tmp);
    }

    public Reservation find(Integer id) {
        return reservationRepository.getById(id);
    }

    @Override
    public List<Reservation> findAllReservations(User user) {
        return reservationRepository.findAllUsersReservations(user);
    }

    @Override
    public List<Reservation> findMyAllReservations(User user) {
        return reservationRepository.findAllUsersReservations(user.getId());
    }

    @Override
    public List<Reservation> findAllReservations(User user, LocalDate fromDate, LocalDate toDate) {
        List<Reservation> allReservations = reservationRepository.findAllUsersReservations(user);
        return this.filterReservations(allReservations, fromDate, toDate);
    }

    public List<Reservation> findAllReservations(User user, LocalDate date) {
        List<Reservation> allReservations = reservationRepository.findAllUsersReservations(user);
        return this.filterReservations(allReservations, date, date);
    }

    /**
     * > Find all reservations that are unpaid for a given user
     *
     * @param user the user that is logged in
     * @return List of all unpaid reservations for a user
     */
    @Override
    public List<Reservation> findAllUnpaidReservations(User user) {
        return reservationRepository.findAllUsersUnpaidReservations(user.getId());
    }

    /**
     * Find all reservations for a given reservation system.
     *
     * @param reservationSystem The reservation system that the reservation belongs to.
     * @return A list of all reservations for a given reservation system.
     */
    @Override
    public List<Reservation> findAllReservations(ReservationSystem reservationSystem) {
        return reservationRepository.findAllReservationsForReservationSystem(reservationSystem.getId());
    }


    /**
     * Find all reservations in the given reservation system, and then filter them by the given date range.
     *
     * @param reservationSystem The reservation system to search for reservations in.
     * @param from The start date of the period to search for reservations.
     * @param to The end date of the period for which you want to find reservations.
     * @return A list of reservations that are within the specified date range.
     */
    @Override
    public List<Reservation> findAllReservations(ReservationSystem reservationSystem, LocalDate from, LocalDate to) {
        List<Reservation> allReservations = this.findAllReservations(reservationSystem);
        return filterReservations(allReservations, from, to);
    }

    /**
     * @param reservations
     * @param fromDate
     * @param toDate
     * @return new list of reservations which are between the given dates (inclusive)
     */
    private List<Reservation> filterReservations(List<Reservation> reservations, LocalDate fromDate, LocalDate toDate){
        LocalDate helperFrom = fromDate.minusDays(1);
        LocalDate helperTo = toDate.plusDays(1);

        List<Reservation> filtered = reservations.stream().
                filter(e -> (e.getReservationSlot().getDate().isEqual(fromDate) || e.getReservationSlot().getDate().isAfter(helperFrom))
                        && (e.getReservationSlot().getDate().isEqual(toDate) || e.getReservationSlot().getDate().isBefore(helperTo)) )
                .collect(Collectors.toList());

        return filtered;
    }
}
