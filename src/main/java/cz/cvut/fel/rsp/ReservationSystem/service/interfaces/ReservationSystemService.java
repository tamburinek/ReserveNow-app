package cz.cvut.fel.rsp.ReservationSystem.service.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;

import java.util.List;

public interface ReservationSystemService{
    public void createReservationSystem(User user, ReservationSystem reservationSystem);

    public void addManager(User user, ReservationSystem reservationSystem);

    public List<ReservationSystem> findAll();

    public ReservationSystem find(Integer id);

    public List<Source> getSources(ReservationSystem reservationSystem);
}
