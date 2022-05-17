package cz.cvut.fel.rsp.ReservationSystem.dao.service;

import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationSystemRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.UserRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.testutil.Generator;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.ReservationSystemServiceImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.SourceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationSystemServiceImplTest {

    @Autowired
    private ReservationSystemServiceImpl reservationSystemService;

    @Autowired
    private ReservationSystemRepository reservationSystemRepository;

    @Autowired
    private SourceServiceImpl sourceService;

    @Autowired
    private UserRepository userRepository;

    private User owner, employee;

    private ReservationSystem reservationSystem;

    @BeforeEach
    public void init() {
        owner = Generator.generateSystemOwner();
        employee = Generator.generateEmployeeUser();
        userRepository.save(owner);
        userRepository.save(employee);
        reservationSystem = new ReservationSystem();
        reservationSystem.setName("Test system");
    }

    @Test
    public void createReservationSystem_createCorrectSystem_systemCreated() {
        reservationSystemService.createReservationSystem(owner, reservationSystem);
        ReservationSystem result = reservationSystemRepository.findById(reservationSystem.getId()).orElse(null);

        Assertions.assertNotNull(result);
    }

    @Test
    public void createReservationSystem_createCorrectSystem_systemHasOneManager() {
        reservationSystemService.createReservationSystem(owner, reservationSystem);
        ReservationSystem result = reservationSystemRepository.findById(reservationSystem.getId()).orElse(null);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getManagers().contains(owner));
    }

    @Test
    public void createReservationSystem_createSystemByEmployeeUser_exceptionThrown() {
        Assertions.assertThrows(ReservationSystemException.class,
                () -> reservationSystemService.createReservationSystem(employee, reservationSystem));
    }

    @Test
    public void addManager_addEmployeeUser_employeeAdded() {
        reservationSystemService.createReservationSystem(owner, reservationSystem);
        reservationSystemService.addManager(employee, reservationSystem);

        ReservationSystem system = reservationSystemRepository.findById(reservationSystem.getId()).orElse(null);

        Assertions.assertNotNull(system);
        Assertions.assertTrue(system.getManagers().contains(employee));
        Assertions.assertTrue(system.getManagers().contains(owner));
    }

    @Test
    public void addManager_addEmployeeTwice_exceptionThrown() {
        reservationSystemService.createReservationSystem(owner, reservationSystem);
        reservationSystemService.addManager(employee, reservationSystem);

        Assertions.assertThrows(ReservationSystemException.class,
                () -> reservationSystemService.addManager(employee, reservationSystem));
    }

    @Test
    public void findAll_findsAllReservationSystem_allReservationSystemFounds(){
        reservationSystemService.createReservationSystem(owner, reservationSystem);

        ReservationSystem reservationSystem2 = Generator.generateReservationSystem(null, null);
        ReservationSystem reservationSystem3 = Generator.generateReservationSystem(null, null);

        reservationSystemService.createReservationSystem(owner, reservationSystem2);
        reservationSystemService.createReservationSystem(owner, reservationSystem3);

        List<ReservationSystem> result = reservationSystemService.findAll();
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void findById_findCorrectReservationSystem_returnCorrectReservationSystem(){
        reservationSystemService.createReservationSystem(owner, reservationSystem);

        ReservationSystem result = reservationSystemService.find(reservationSystem.getId());

        Assertions.assertEquals(reservationSystem, result);
    }

    @Test
    public void findSources_findsAllSources_returnAllReservationSystemSources(){
        reservationSystemService.createReservationSystem(owner, reservationSystem);

        Source source1 = Generator.generateSource(reservationSystem, null);
        Source source2 = Generator.generateSource(reservationSystem, null);

        sourceService.createSource(source1, reservationSystem);
        sourceService.createSource(source2, reservationSystem);

        List<Source> result = reservationSystemService.getSources(reservationSystem);

        Assertions.assertEquals(2, result.size());
    }

}
