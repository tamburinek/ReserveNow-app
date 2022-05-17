package cz.cvut.fel.rsp.ReservationSystem.service.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.CashRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.WireRepository;
import cz.cvut.fel.rsp.ReservationSystem.exception.PaymentException;
import cz.cvut.fel.rsp.ReservationSystem.model.payment.Cash;
import cz.cvut.fel.rsp.ReservationSystem.model.payment.Wire;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final CashRepository cashDao;
    private final WireRepository wireDao;
    private final ReservationRepository reservationDao;

    public PaymentServiceImpl(CashRepository cashDao, WireRepository wireDao, ReservationRepository reservationDao) {
        this.cashDao = cashDao;
        this.wireDao = wireDao;
        this.reservationDao = reservationDao;
    }

    @Override
    public void createCashPayment(Reservation reservation) {
        if (reservation == null) {
            throw new PaymentException("Reservation you want to pay for does not exist.");
        }
        if (reservation.isCancelled()) {
            throw new PaymentException("You are trying to pay for canceled reservation.");
        }
        Cash cash = new Cash();
        cash.setAmount(reservation.getReservationSlot().getPrice());
        cash.setDateTimePaid(LocalDateTime.now());
        cash.setReservation(reservation);
        reservation.setPayment(cash);
        cashDao.save(cash);
        reservationDao.save(reservation);
    }

    @Override
    public void createWirePayment(Reservation reservation) {
        if (reservation == null) {
            throw new PaymentException("Reservation you want to pay for does not exist.");
        }
        if (reservation.isCancelled()) {
            throw new PaymentException("You are trying to pay for canceled reservation.");
        }
        Wire wire = new Wire();
        wire.setAmount(reservation.getReservationSlot().getPrice());
        wire.setDateTimePaid(LocalDateTime.now());
        wire.setReservation(reservation);
        reservation.setPayment(wire);
        wireDao.save(wire);
        reservationDao.save(reservation);
    }
}
