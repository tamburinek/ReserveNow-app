package cz.cvut.fel.rsp.ReservationSystem.dao;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.Seat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class GeneralRepositoryTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    public void persist_persistOneEntity_entityPersisted() {
        Seat seat = new Seat();
        seat.setSeatIdentifier("x");
        seat.setPrice(100);

        seatRepository.save(seat);
        final Seat result = em.find(Seat.class, seat.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(100, result.getPrice());
        Assertions.assertEquals("x", result.getSeatIdentifier());
    }
}
