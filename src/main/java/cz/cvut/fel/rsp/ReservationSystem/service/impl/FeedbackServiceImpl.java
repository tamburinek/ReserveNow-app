package cz.cvut.fel.rsp.ReservationSystem.service.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.FeedbackRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.ReservationSystemRepository;
import cz.cvut.fel.rsp.ReservationSystem.exception.FeedbackException;
import cz.cvut.fel.rsp.ReservationSystem.model.Feedback;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository dao;
    private final ReservationSystemRepository reservationSystemDao;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository dao, ReservationSystemRepository reservationSystemDao) {
        this.dao = dao;
        this.reservationSystemDao = reservationSystemDao;
    }

    //show all feedbacks is implemented in reservationSystemServiceImpl !!!

    @Override
    public void createFeedback(Feedback feedback, ReservationSystem reservationSystem) {
        if (feedback.getMessage() == null) {
            throw new FeedbackException("Feedback has to have content.");
        }
        reservationSystem.getFeedback().add(feedback);

        dao.save(feedback);
        reservationSystemDao.save(reservationSystem);
    }

    @Override
    public void createFeedbacks(List<Feedback> feedbacks, ReservationSystem reservationSystem) {
        for (Feedback feedback : feedbacks) {
            if (feedback.getMessage() == null) {
                throw new FeedbackException("Feedback has to have content.");
            }
        }
        reservationSystem.getFeedback().addAll(feedbacks);
        dao.saveAll(feedbacks);
        reservationSystemDao.save(reservationSystem);
    }

    @Override
    public void deleteFeedback(ReservationSystem reservationSystem, Feedback feedback) {
        dao.delete(feedback);
        if (reservationSystem.getFeedback().contains(feedback)) {
            reservationSystem.getFeedback().remove(feedback);
            reservationSystemDao.save(reservationSystem);
        } else {
            throw new FeedbackException("This is not feedback of this reservation system.");
        }
    }
}
