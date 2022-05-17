package cz.cvut.fel.rsp.ReservationSystem.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import cz.cvut.fel.rsp.ReservationSystem.dao.AddressRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.UserRepository;
import cz.cvut.fel.rsp.ReservationSystem.model.Feedback;
import cz.cvut.fel.rsp.ReservationSystem.model.enums.Repetition;
import cz.cvut.fel.rsp.ReservationSystem.model.enums.UserType;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.*;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.CustomTimeEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.IntervalEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.SeatEvent;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.slots.ReservationSlot;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.SystemInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SystemInitializerImpl implements SystemInitializer {

    private final ReservationSystemServiceImpl reservationSystemService;

    private final ReservationSlotServiceImpl reservationSlotService;

    private final ReservationServiceImpl reservationService;

    private final SourceServiceImpl sourceService;

    private final EventServiceImpl eventService;

    private final FeedbackServiceImpl feedbackService;

    private final UserRepository userRepository; // TODO change for user service later

    private final AddressRepository addressRepository;

    private final Environment environment;

    @Autowired(required = false) // Required = false because of tests
    PasswordEncoder encoder;

    @Override
    @PostConstruct
    public void initSystem() {
        if (Arrays.asList(environment.getActiveProfiles()).contains("testprofile")) {
            return;
        }
        if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            log.info("prod env, skip init");
            return;
        }

        List<String[]> userRecords = readCsvData("src/main/resources/generatorCSVs/users.csv");
        List<String[]> systemRecords = readCsvData("src/main/resources/generatorCSVs/reservation_systems.csv");
        List<String[]> addressRecords = readCsvData("src/main/resources/generatorCSVs/addresses.csv");
        List<String[]> sourceRecords = readCsvData("src/main/resources/generatorCSVs/sources.csv");
        List<String[]> seatEventRecords = readCsvData("src/main/resources/generatorCSVs/seatEvents.csv");
        List<String[]> customTimeEventRecords = readCsvData("src/main/resources/generatorCSVs/customTimeEvents.csv");
        List<String[]> intervalEventRecords = readCsvData("src/main/resources/generatorCSVs/intervalEvents.csv");
        List<String[]> reservationRecords = readCsvData("src/main/resources/generatorCSVs/reservations.csv");
        List<String[]> feedbackRecords = readCsvData("src/main/resources/generatorCSVs/feedbacks.csv");

        List<User> users = generateUsers(userRecords);
        List<ReservationSystem> systems = generateReservationSystems(systemRecords, users);
        List<Address> addresses = generateAddresses(addressRecords);
        List<Source> sources = generateSources(sourceRecords, addresses, systems);
        List<Event> events = generateEvents(seatEventRecords, customTimeEventRecords, intervalEventRecords, sources);

        generateReservations(reservationRecords, users, events);
        generateFeedbacks(feedbackRecords, systems);
    }

    private List<String[]> readCsvData(String path) {
        log.info("Reading CSV data from " + path);
        List<String[]> csvData;
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            csvData = csvReader.readAll();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        return csvData;
    }

    private List<User> generateUsers(List<String[]> userRecords) {
        log.info("Generating users");
        List<User> users = new ArrayList<>();
        for (String[] userData : userRecords) {
            User user = new User();
            user.setFirstName(userData[1]);
            user.setLastName(userData[2]);
            user.setUsername(userData[4]);
            switch (userData[5]) {
                case "ROLE_ADMIN":
                    user.setUserType(UserType.ROLE_ADMIN);
                    break;
                case "ROLE_SYSTEM_OWNER":
                    user.setUserType(UserType.ROLE_SYSTEM_OWNER);
                    break;
                case "ROLE_SYSTEM_EMPLOYEE":
                    user.setUserType(UserType.ROLE_SYSTEM_EMPLOYEE);
                    break;
                default:
                    user.setUserType(UserType.ROLE_REGULAR_USER);
                    break;
            }
            user.setEmail(userData[0]);
            user.setPassword(encoder.encode(userData[3]));
            userRepository.save(user);
            users.add(user);
        }
        return users;
    }

    private List<ReservationSystem> generateReservationSystems(List<String[]> systemRecords, List<User> users) {
        log.info("Generating reservation systems");
        int counter = 0;
        List<ReservationSystem> systems = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType() == UserType.ROLE_SYSTEM_OWNER) {
                ReservationSystem reservationSystem = new ReservationSystem();
                reservationSystem.setName(systemRecords.get(counter++)[0]);
                reservationSystemService.createReservationSystem(user, reservationSystem);
                systems.add(reservationSystem);
            }
        }
        return systems;
    }

    private List<Address> generateAddresses(List<String[]> addressRecords) {
        log.info("Generating addresses");
        List<Address> addresses = new ArrayList<>();
        for (String[] addressData : addressRecords) {
            Address address = new Address();
            address.setCity(addressData[0]);
            address.setStreet(addressData[3]);
            address.setHouseNumber(addressData[1]);
            address.setPostalCode(addressData[2]);
            addressRepository.save(address);
            addresses.add(address);
        }
        return addresses;
    }

    private List<Source> generateSources(List<String[]> sourceRecords, List<Address> addresses, List<ReservationSystem> systems) {
        log.info("Generating sources");
        List<Source> sources = new ArrayList<>();
        int[] values = {3, 1, 4, 4, 3, 4, 4, 2, 3, 2};
        int[] categories = {2, 3, 3, 0, 4, 2, 3, 2, 1, 4, 2, 3, 1, 3, 0, 4, 3, 2, 1, 3, 4, 3, 4, 3, 0, 4, 4, 2, 1, 4};
        String[] categoryNames = {"Pubs", "Theatre", "Restaurants", "Sport", "Other"};
        int counter = 0;
        for (int i = 0; i < sourceRecords.size(); i++) {
            Source source = new Source();
            source.setActive(true);
            source.setAddress(addresses.get(i));
            source.setName(sourceRecords.get(i)[1]);
            source.setDescription(sourceRecords.get(i)[0]);
            sources.add(source);
        }
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i]; j++) {
                sourceService.createSource(sources.get(counter++), systems.get(i), categoryNames[categories[i]]);
            }
        }
        return sources;
    }

    private List<Event> generateEvents(List<String[]> seatEventRecords, List<String[]> customTimeEventRecords, List<String[]> intervalEventRecords, List<Source> sources) {
        log.info("Generating events");
        List<Event> events = new ArrayList<>();
        for (String[] seatEventData : seatEventRecords) {
            SeatEvent seatEvent = new SeatEvent();
            seatEvent.setName(seatEventData[3]);
            seatEvent.setStartDate(LocalDate.parse(seatEventData[6]));
            seatEvent.setDay(seatEvent.getStartDate().getDayOfWeek());
            seatEvent.setRepetition(Repetition.NONE);
            seatEvent.setFromTime(LocalTime.parse(seatEventData[2]));
            seatEvent.setToTime(LocalTime.parse(seatEventData[7]));
            seatEvent.setSeatAmount(Integer.valueOf(seatEventData[9]));
            eventService.createEvent(seatEvent, sources.get(Integer.parseInt(seatEventData[8]) - 1).getCategories().get(0));
            events.add(seatEvent);
        }
        for (String[] customTimeEventData : customTimeEventRecords) {
            CustomTimeEvent customTimeEvent = new CustomTimeEvent();
            customTimeEvent.setName(customTimeEventData[3]);
            customTimeEvent.setStartDate(LocalDate.parse(customTimeEventData[6]));
            customTimeEvent.setDay(customTimeEvent.getStartDate().getDayOfWeek());
            customTimeEvent.setRepetition(Repetition.NONE);
            customTimeEvent.setFromTime(LocalTime.parse(customTimeEventData[2]));
            customTimeEvent.setToTime(LocalTime.parse(customTimeEventData[7]));
            customTimeEvent.setMinimalReservationTime(Duration.ofMinutes(30));
            eventService.createEvent(customTimeEvent, sources.get(Integer.parseInt(customTimeEventData[8]) - 1).getCategories().get(0));
            events.add(customTimeEvent);
        }
        for (String[] intervalEventData : intervalEventRecords) {
            IntervalEvent intervalEvent = new IntervalEvent();
            intervalEvent.setName(intervalEventData[3]);
            intervalEvent.setStartDate(LocalDate.parse(intervalEventData[6]));
            intervalEvent.setDay(intervalEvent.getStartDate().getDayOfWeek());
            intervalEvent.setRepetition(Repetition.NONE);
            intervalEvent.setFromTime(LocalTime.parse(intervalEventData[2]));
            intervalEvent.setToTime(LocalTime.parse(intervalEventData[7]));
            intervalEvent.setIntervalDuration(Duration.ofHours(1));
            intervalEvent.setTimeBetweenIntervals(Duration.ZERO);
            eventService.createEvent(intervalEvent, sources.get(Integer.parseInt(intervalEventData[8]) - 1).getCategories().get(0));
            events.add(intervalEvent);
        }
        return events;
    }

    private void generateReservations(List<String[]> reservationRecords, List<User> users, List<Event> events) {
        log.info("Generating reservations");
        List<ReservationSlot> slots = new ArrayList<>();
        for (Event event : events) {
            slots.addAll(reservationSlotService.findAllFree(event));
        }
        for (String[] reservationData : reservationRecords) {
            User user = users.stream().filter(u -> Objects.equals(u.getId(), Integer.valueOf(reservationData[4]))).findAny().orElse(null);
            ReservationSlot slot = slots.stream().filter(s -> Objects.equals(s.getId(), Integer.valueOf(reservationData[3]))).findAny().orElse(null);
            slots.remove(slot);
            reservationService.createReservation(user, slot);
        }
    }

    private void generateFeedbacks(List<String[]> feedbackRecords, List<ReservationSystem> systems) {
        log.info("Generating feedbacks");
        for (ReservationSystem system : systems) {
            List<String[]> systemFeedbacks = feedbackRecords.stream().filter(f -> Integer.valueOf(f[1]).equals(system.getId())).collect(Collectors.toList());
            List<Feedback> feedbacks = new ArrayList<>();
            for (String[] feedbackData : systemFeedbacks) {
                Feedback feedback = new Feedback();
                feedback.setMessage(feedbackData[0]);
                feedbacks.add(feedback);
            }
            feedbackService.createFeedbacks(feedbacks, system);
        }
    }
}
