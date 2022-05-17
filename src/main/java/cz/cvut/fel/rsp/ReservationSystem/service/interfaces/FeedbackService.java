package cz.cvut.fel.rsp.ReservationSystem.service.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.model.Feedback;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;

import java.util.List;

public interface FeedbackService{
    public void createFeedback(Feedback feedback, ReservationSystem reservationSystem);

    public void createFeedbacks(List<Feedback> feedbacks, ReservationSystem reservationSystem);

    public void deleteFeedback(ReservationSystem reservationSystem, Feedback feedback);
}
