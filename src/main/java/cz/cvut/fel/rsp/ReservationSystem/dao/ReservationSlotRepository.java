package cz.cvut.fel.rsp.ReservationSystem.dao;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationSlotRepository extends JpaRepository<ReservationSlot, Integer> {

    @Query(value = "SELECT r FROM ReservationSlot r WHERE r.event.id = ?1")
    public List<ReservationSlot> findByEventId(Integer eventId);

    @Query(value = "SELECT rs FROM ReservationSlot rs JOIN Reservation re ON (re.reservationSlot.id = rs.id AND re.cancelled = false)" +
            "WHERE rs.event.id = ?1")
    public List<ReservationSlot> findReservedByEventId(Integer eventId);

    @Query(value = "SELECT rs FROM ReservationSlot rs WHERE rs.event.id = ?1 AND rs NOT IN " +
                    "(SELECT rs FROM ReservationSlot rs JOIN Reservation re ON (re.reservationSlot.id = rs.id AND re.cancelled = false) " +
                    "WHERE rs.event.id = ?1)")
    public List<ReservationSlot> findFreeByEventId(Integer eventId);

    @Query(value = "SELECT r FROM ReservationSlot r WHERE r.event.id = :event_id AND r.date >= :from_date AND r.date <= :to_date")
    public List<ReservationSlot> findByEventId(@Param("event_id") Integer eventId, @Param("from_date") LocalDate from, @Param("to_date") LocalDate to);

    @Query(value = "SELECT rs FROM ReservationSlot rs JOIN Reservation re ON (re.reservationSlot.id = rs.id AND re.cancelled = false) " +
            "WHERE rs.event.id = :event_id AND rs.date >= :from_date AND rs.date <= :to_date")
    public List<ReservationSlot> findReservedByEventId(@Param("event_id") Integer eventId, @Param("from_date") LocalDate from, @Param("to_date") LocalDate to);

    @Query(value = "SELECT rs FROM ReservationSlot rs WHERE rs.event.id = :event_id AND rs NOT IN " +
            "(SELECT rs FROM ReservationSlot rs JOIN Reservation re ON (re.reservationSlot.id = rs.id AND re.cancelled = false) " +
            "WHERE rs.event.id = :event_id)" +
            "AND rs.date >= :from_date AND rs.date <= :to_date")
    public List<ReservationSlot> findFreeByEventId(@Param("event_id") Integer eventId, @Param("from_date") LocalDate from, @Param("to_date") LocalDate to);
}
