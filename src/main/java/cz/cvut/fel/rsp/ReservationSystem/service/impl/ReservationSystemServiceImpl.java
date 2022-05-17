package cz.cvut.fel.rsp.ReservationSystem.service.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.PaymentDetailsRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationSystemRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.SourceRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.UserRepository;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.Feedback;
import cz.cvut.fel.rsp.ReservationSystem.model.enums.UserType;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationSystemServiceImpl implements ReservationSystemService {

    private final ReservationSystemRepository reservationSystemRepository;
    private final UserRepository userRepository;
    private final SourceRepository sourceRepository;

    @Override
    public void createReservationSystem(User user, ReservationSystem reservationSystem) {
        if (user.getUserType() != UserType.ROLE_SYSTEM_OWNER) {
            throw new ReservationSystemException("User creating a system must have a system owner account.");
        }
        addManager(user, reservationSystem);
        List<Feedback> feedbacks = new ArrayList<>();
        reservationSystem.setFeedback(feedbacks);
        reservationSystemRepository.save(reservationSystem);
    }

    @Override
    public void addManager(User user, ReservationSystem reservationSystem) {
        List<User> managers = reservationSystem.getManagers();
        if (Objects.isNull(managers)) {
            managers = new ArrayList<>();
            reservationSystem.setManagers(managers);
        }

        if (managers.contains(user) && user.getManages().contains(reservationSystem)) {
            throw new ReservationSystemException("User already manages system " + reservationSystem.getName());
        }

        List<ReservationSystem> userManages = user.getManages();
        if (Objects.isNull(userManages)) {
            userManages = new ArrayList<>();
            user.setManages(userManages);
        }

        managers.add(user);
        userManages.add(reservationSystem);
        reservationSystemRepository.save(reservationSystem);
        userRepository.save(user);
    }

    @Override
    public List<ReservationSystem> findAll() {
        return reservationSystemRepository.findAll();
    }

    @Override
    public ReservationSystem find(Integer id) {
        return reservationSystemRepository.getById(id);
    }

    @Override
    public List<Source> getSources(ReservationSystem reservationSystem) {
        return sourceRepository.findAllSourcesOfReservationSystem(reservationSystem);
    }
}
