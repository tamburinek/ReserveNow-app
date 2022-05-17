package cz.cvut.fel.rsp.ReservationSystem.dao.testutil;

import cz.cvut.fel.rsp.ReservationSystem.model.Feedback;
import cz.cvut.fel.rsp.ReservationSystem.model.enums.Repetition;
import cz.cvut.fel.rsp.ReservationSystem.model.enums.UserType;
import cz.cvut.fel.rsp.ReservationSystem.model.payment.Cash;
import cz.cvut.fel.rsp.ReservationSystem.model.payment.Wire;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.*;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.IntervalEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.SeatEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.*;
import cz.cvut.fel.rsp.ReservationSystem.model.user.PaymentDetails;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;

import java.time.*;
import java.util.Random;
import java.util.List;

public class Generator {
    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static User generateEmployeeUser() {
        User user = generateGenericUser();
        user.setUserType(UserType.ROLE_SYSTEM_EMPLOYEE);
        return user;
    }

    public static User generateRegularUser() {
        User user = generateGenericUser();
        user.setUserType(UserType.ROLE_REGULAR_USER);
        return user;
    }

    public static User generateSystemOwner() {
        User user = generateGenericUser();
        user.setUserType(UserType.ROLE_SYSTEM_OWNER);
        return user;
    }

    private static User generateGenericUser() {
        final User user = new User();
        user.setUsername("username" + randomInt());
        user.setFirstName("FirstName" + randomInt());
        user.setLastName("LastName" + randomInt());
        user.setPassword(Integer.toString(randomInt()));
        user.setEmail(user.getUsername() + "@reservationSystem.cz");
        user.setPaymentDetails(generatePaymentDetails(user));
        return user;
    }

    public static PaymentDetails generatePaymentDetails(User user){
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setUser(user);
        paymentDetails.setCreditCardNumber(Integer.toString(randomInt()));
        return paymentDetails;
    }

    public static Interval generateReservationSlotInterval(){
        Interval interval = new Interval();
        interval.setPrice(randomInt());
        interval.setStart(LocalTime.now());
        interval.setEnd(LocalTime.MAX);
        return interval;
    }

    public static CustomTime generateReservationSlotCustomTime(){
        CustomTime customTime = new CustomTime();
        customTime.setPrice(randomInt());
        customTime.setStart(LocalTime.now());
        customTime.setEnd(LocalTime.MAX);
        // Duration duration = Duration.between(LocalTime.now(),LocalTime.MAX);
        // customTime.setTimeBetweenReservations(duration);
        return customTime;
    }

    public static Seat generateReservationSlotSeat(){
        Seat seat = new Seat();
        seat.setPrice(randomInt());
        seat.setSeatIdentifier("seatId" + randomInt());
        return seat;
    }

    // TODO - problem stejne duration
    public static FixedLengthCustomTime generateReservationSlotFixedLengthCustomTime(){
        FixedLengthCustomTime fixedLengthCustomTime = new FixedLengthCustomTime();
        fixedLengthCustomTime.setPrice(randomInt());
        fixedLengthCustomTime.setStart(LocalTime.now());
        fixedLengthCustomTime.setEnd(LocalTime.MAX);
        // Duration duration = Duration.between(LocalTime.now(),LocalTime.MAX);
        // fixedLengthCustomTime.setTimeBetweenReservations(duration);
        // fixedLengthCustomTime.setFixedLength(duration);
        return fixedLengthCustomTime;
    }

    public static Reservation generateReservation(ReservationSlot reservationSlot){
        Reservation reservation = new Reservation();
        User user = generateRegularUser();
        reservation.setUser(user);
        reservation.setReservationSlot(reservationSlot);
        reservation.setCancelled(false);
        reservation.setAdditionalInfo("addtionalInfo" + randomInt());
        return reservation;

    }

    public static Cash generateCash(Reservation reservation){
        Cash cash = new Cash();
        cash.setAmount(reservation.getReservationSlot().getPrice());
        cash.setDateTimePaid(LocalDateTime.now());
        cash.setReservation(reservation);
        cash.setFoo("foo" + randomInt());
        return cash;
    }

    public static Wire generateWire(Reservation reservation){
        Wire wire = new Wire();
        wire.setAmount(reservation.getPayment().getAmount());
        wire.setDateTimePaid(LocalDateTime.now());
        wire.setReservation(reservation);
        wire.setFoo("foo" + randomInt());
        return wire;
    }

    public static Address generateAddress(){
        Address address = new Address();
        address.setCity("city" + randomInt());
        address.setStreet("street" + randomInt());
        address.setHouseNumber("house" + randomInt());
        address.setHouseNumber("postalNumber" + randomInt());
        return address;
    }

    // TODO others repetition - udelat x metod nebo si to kazdy v testech prenastavi podle potreby?
    public static Event generateIntervalEventWithoutRepetition(){
        IntervalEvent event = new IntervalEvent();
        event.setIntervalDuration(Duration.ofHours(1));
        event.setTimeBetweenIntervals(Duration.ofHours(1));
        event.setName("event" + randomInt());
        event.setFromTime(LocalTime.of(1,0));
        event.setToTime(LocalTime.of(4,0));
        event.setRepeatUntil(LocalDate.MAX);
        event.setDay(DayOfWeek.of(RAND.nextInt(7) + 1));
        event.setRepetition(Repetition.NONE);
        event.setStartDate(LocalDate.of(2023, 10, 10));
        return event;
    }

    public static Event generateIntervalEventWithRepetition() {
        Event event = generateIntervalEventWithoutRepetition();
        event.setRepetition(Repetition.DAILY);
        return event;
    }

    public static SeatEvent generateSeatEventWithoutRepetition() {
        SeatEvent event = new SeatEvent();
        event.setSeatAmount(15);
        event.setName("event" + randomInt());
        event.setFromTime(LocalTime.of(1,0));
        event.setToTime(LocalTime.of(4,0));
        event.setRepeatUntil(LocalDate.MAX);
        event.setDay(DayOfWeek.of(RAND.nextInt(7) + 1));
        event.setRepetition(Repetition.NONE);
        event.setStartDate(LocalDate.of(2023, 10, 10));
        return event;
    }

    // Nenastavuje id
    public static Source generateSource(ReservationSystem reservationSystem, Address address){
        Source source = new Source();
        source.setName("source" + randomInt());
        source.setDescription("sourceDescription" + randomInt());
        source.setAddress(address);
        source.setActive(true);
        source.setReservationSystem(reservationSystem);
        return source;
    }



    public static ReservationSystem generateReservationSystem(List<User> managers, List<Feedback> feedback){
        ReservationSystem reservationSystem = new ReservationSystem();
        reservationSystem.setName("reservationSystem" + randomInt());
        reservationSystem.setManagers(managers);
        reservationSystem.setFeedback(feedback);
        return reservationSystem;
    }

    public static Category generateCategory(){
        Category category = new Category();
        category.setName("category" + randomInt());
        return category;
    }

//    public static List<Category> generateCategorie(int count){
//        List<Category> categories = new ArrayList<>();
//        for(int i = 0; i < count; i++){
//            categories.add(generateCategory());
//        }
//        return categories;
//    }

    public static Feedback generateFeedback(){
        Feedback feedback = new Feedback();
        feedback.setMessage("feedback" + randomInt());
        return feedback;
    }

//    public static List<Feedback> generateFeedback(int count){
//        List<Feedback> feedback = new ArrayList<>();
//        for(int i = 0; i < count; i++){
//            feedback.add(generateFeedback());
//        }
//        return feedback;
//    }
}
