package cz.cvut.fel.rsp.ReservationSystem.service.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;

import java.time.LocalTime;

public interface PaymentService{
    public void createCashPayment(Reservation reservation);

    public void createWirePayment(Reservation reservation);
}
