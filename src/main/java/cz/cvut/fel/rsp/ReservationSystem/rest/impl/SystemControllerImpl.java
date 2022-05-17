package cz.cvut.fel.rsp.ReservationSystem.rest.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.AddressRepository;
import cz.cvut.fel.rsp.ReservationSystem.model.Feedback;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.*;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.*;
import cz.cvut.fel.rsp.ReservationSystem.rest.interfaces.SystemController;
import cz.cvut.fel.rsp.ReservationSystem.rest.util.RestUtil;
import cz.cvut.fel.rsp.ReservationSystem.security.services.UserDetailsImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/")
@Slf4j
@RequiredArgsConstructor
public class SystemControllerImpl implements SystemController {

    private final ReservationSystemServiceImpl reservationSystemService;

    private final ReservationServiceImpl reservationService;

    private final FeedbackServiceImpl feedbackService;

    private final SourceServiceImpl sourceService;

    private final EventServiceImpl eventService;

    private final UserServiceImpl userService;

    private final AddressRepository addressRepository;

    @Override
    @GetMapping(value = "/systems", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationSystemDTO> getReservationSystems() {
        return reservationSystemService.findAll().stream()
                .map(ReservationSystemDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping(value = "/systems")
    public ResponseEntity<Void> createSystem(@RequestBody ReservationSystemDTO reservationSystemDTO) {
        ReservationSystem reservationSystem = mapReservationSystem(reservationSystemDTO);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User loggedUser = userService.findByUsername(userDetails.getUsername());

        reservationSystemService.createReservationSystem(loggedUser, reservationSystem);
        log.info("Created reservation system: {} for user: {}", reservationSystem, loggedUser);

        final HttpHeaders headers = RestUtil.createLocationHeaderFromCurrentUri("/{id}", reservationSystem.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Override
    @GetMapping(value = "/systems/{systemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationSystemDTO getById(@PathVariable Integer systemId) {
        return new ReservationSystemDTO(reservationSystemService.find(systemId));
    }

    @GetMapping(value = "/systems/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationSystemDTO getMyReservationSystem() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        ReservationSystem reservationSystem = userService.findMyReservationSystem(user);
        return new ReservationSystemDTO(reservationSystem);
    }

    @Override
    @GetMapping(value = "/systems/{systemId}/sources", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SourceDTO> getSources(@PathVariable Integer systemId) {
        ReservationSystem reservationSystem = reservationSystemService.find(systemId);

        return reservationSystemService.getSources(reservationSystem).stream()
                .map(SourceDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @GetMapping(value = "/systems/my/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Feedback> getFeedback() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        ReservationSystem reservationSystem = userService.findMyReservationSystem(user);

        return reservationSystem.getFeedback();
    }

    @GetMapping(value = "/systems/{systemId}/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Feedback> getSystemFeedback(@PathVariable Integer systemId) {
        ReservationSystem reservationSystem = reservationSystemService.find(systemId);
        return reservationSystem.getFeedback();
    }

    @GetMapping(value = "/systems/{systemId}/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getCustomers(@PathVariable Integer systemId) {
        ReservationSystem reservationSystem = reservationSystemService.find(systemId);
        List<Reservation> reservations = reservationService.findAllReservations(reservationSystem);
        List<UserDTO> users = new ArrayList<>();
        for (Reservation reservation : reservations) {
            User userik = reservation.getUser();
            if (!users.contains(userik)) {
                users.add(new UserDTO(userik));
            }
        }
        return users;
    }

    @GetMapping(value = "/systems/my/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getCustomers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        ReservationSystem reservationSystem = userService.findMyReservationSystem(user);

        List<Reservation> reservations = reservationService.findAllReservations(reservationSystem);
        List<User> users = new ArrayList<>();
        List<UserDTO> result = new ArrayList<>();
        for (Reservation reservation : reservations) {
            User userik = reservation.getUser();
            if (!users.contains(userik)) {
                users.add(userik);
            }
        }
        for (User userTmp : users){
                result.add(new UserDTO(userTmp));
        }
        return result;
    }

    @GetMapping(value = "/systems/my/sources", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SourceDTO> getSources() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        ReservationSystem reservationSystem = userService.findMyReservationSystem(user);

        List<SourceDTO> sourceDTOS = new ArrayList<>();
        for (Source source : reservationSystemService.getSources(reservationSystem)) {
            sourceDTOS.add(new SourceDTO(source));
        }

        return sourceDTOS;
    }

    @Override
    @PostMapping(value = "/systems/{systemId}/feedback")
    public ResponseEntity<Void> createFeedback(@PathVariable Integer systemId, @RequestBody Feedback feedback) {
        ReservationSystem reservationSystem = reservationSystemService.find(systemId);
        feedbackService.createFeedback(feedback, reservationSystem);
        log.info("Created feedback for system with id {} with following message: {}", systemId, feedback.getMessage());
        final HttpHeaders headers = RestUtil.createLocationHeaderFromCurrentUri("/{feedbackId}", feedback.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Override
    @PostMapping(value = "/systems/my/sources")
    public ResponseEntity<Void> createSource(@RequestBody SourceDTO sourceDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        ReservationSystem reservationSystem = userService.findMyReservationSystem(user);

        Address address = new Address();
        address.setCity(sourceDTO.getAddress().getCity());
        address.setStreet(sourceDTO.getAddress().getStreet());
        address.setHouseNumber(sourceDTO.getAddress().getHouseNumber());
        address.setPostalCode(sourceDTO.getAddress().getPostalCode());
        addressRepository.save(address);

        Source source = new Source();
        source.setName(sourceDTO.getName());
        source.setDescription(sourceDTO.getDescription());
        source.setActive(true);
        source.setAddress(address);
        sourceService.createSource(source, reservationSystem);
        log.info("Created source {} for system with id {}.", source, reservationSystem);
        final HttpHeaders headers = RestUtil.createLocationHeaderNewUri("/sources/{sourceId}", source.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Override
    @GetMapping(value = "/systems/{systemId}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDTO> getAllReservationsFromTo(@PathVariable Integer systemId,
                                                         @RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                         @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        ReservationSystem reservationSystem = reservationSystemService.find(systemId);
        List<Reservation> reservations = reservationService.findAllReservations(reservationSystem, fromDate, toDate);
        return reservations.stream().map(ReservationDTO::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/systems/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDTO> getAllReservations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        ReservationSystem reservationSystem = userService.findMyReservationSystem(user);
        List<Reservation> reservations = reservationService.findAllReservations(reservationSystem);
        return reservations.stream().map(ReservationDTO::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/systems/reservations/today", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDTO> getAllReservationsToday(@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        ReservationSystem reservationSystem = userService.findMyReservationSystem(user);
        List<Reservation> reservations = reservationService.findAllReservations(reservationSystem, date, date);
        return reservations.stream().map(ReservationDTO::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/systems/{systemId}/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventDTO> getAllEventsFromTo(@PathVariable Integer systemId,
                                             @RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                             @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        ReservationSystem reservationSystem = reservationSystemService.find(systemId);
        List<Event> events = eventService.findAllEvents(reservationSystem, fromDate, toDate);
        return events.stream().map(EventDTO::new).collect(Collectors.toList());
    }


    @GetMapping(value = "/systems/my/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventDTO> getAllMyEventsFromTo(
            @RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        ReservationSystem reservationSystem = userService.findMyReservationSystem(user);
        List<Event> events = eventService.findAllEvents(reservationSystem, fromDate, toDate);
        return events.stream().map(EventDTO::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/systems/my/all/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventDTO> getAllMyEvents() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        ReservationSystem reservationSystem = userService.findMyReservationSystem(user);
        List<Event> events = eventService.findAllEvents(reservationSystem);
        return events.stream().map(EventDTO::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/systems/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventDTO> getAllEventsToFuture() {
        List<ReservationSystem> systems = reservationSystemService.findAll();
        List<Event> events = new ArrayList<>();
        for (ReservationSystem system : systems) {
            events.addAll(eventService.findAllEventsToFuture(system));
        }
        return events.stream().map(EventDTO::new).collect(Collectors.toList());
    }

    private ReservationSystem mapReservationSystem(ReservationSystemDTO reservationSystemDTO) {
        ReservationSystem reservationSystem = new ReservationSystem();
        reservationSystem.setName(reservationSystemDTO.getName());

        if (reservationSystemDTO.getManagers() != null) {
            reservationSystem.setManagers(reservationSystemDTO.getManagers().stream()
                    .map(userService::findByUsername)
                    .collect(Collectors.toList()));
        }

        return reservationSystem;
    }
}
