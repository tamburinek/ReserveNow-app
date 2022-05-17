package cz.cvut.fel.rsp.ReservationSystem.dao;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceRepository extends JpaRepository<Source, Integer> {

    @Query("select p from Source p where p.reservationSystem = ?1")
    public List<Source> findAllSourcesOfReservationSystem(ReservationSystem reservationSystem);
}
