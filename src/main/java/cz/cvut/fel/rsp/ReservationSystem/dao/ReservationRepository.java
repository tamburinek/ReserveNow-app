package cz.cvut.fel.rsp.ReservationSystem.dao;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query(value = "select p from Reservation p where p.user = ?1")
    List<Reservation> findAllUsersReservations(User user);

    @Query(value = "select p from Reservation p where p.user.id = :user_id")
    List<Reservation> findAllUsersReservations(@Param("user_id") int user_id);

    @Query(value = "SELECT r FROM Reservation r WHERE r.id = :reservation_id")
    Reservation findReservationById(@Param("reservation_id") Integer reservationId);

    @Query(value = "SELECT r FROM Reservation r WHERE r.user.id = :user_id AND r.payment IS NULL")
    List<Reservation> findAllUsersUnpaidReservations(@Param("user_id") Integer userId);

    /**
     * Find a reservation that is not cancelled for a given reservation slot.
     *
     * @param slotId The id of the reservation slot
     * @return A Reservation object
     */
    @Query(value = "SELECT r FROM Reservation r where r.reservationSlot.id = :reservationSlot_id AND r.cancelled = false")
    Reservation findNotCancelledReservationForReservationSlot(@Param("reservationSlot_id") Integer slotId);

    /**
     * "Find all reservations for a reservation system."
     *
     * The first thing to notice is that the query is written in JPQL, not SQL. JPQL is a language that is similar to SQL,
     * but it is not SQL. JPQL is a language that is used to query entities
     *
     * @param systemId The id of the reservation system
     * @return A list of reservations for a reservation system.
     */
    @Query(value = "SELECT r FROM Reservation r JOIN ReservationSlot rs ON rs.id = r.reservationSlot" +
            ".id " +
            "JOIN Event e ON rs.event.id = e.id " +
            "JOIN Source s ON e.category IN elements(s.categories)" +
            "WHERE s.reservationSystem.id = :system_Id")
    List<Reservation> findAllReservationsForReservationSystem(@Param("system_Id") Integer systemId);
}
