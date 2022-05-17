package cz.cvut.fel.rsp.ReservationSystem.rest.impl;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.PaymentDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationSystemDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.interfaces.ReservationsController;
import cz.cvut.fel.rsp.ReservationSystem.rest.util.RestUtil;
import cz.cvut.fel.rsp.ReservationSystem.security.services.UserDetailsImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.ReservationServiceImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/v1/")
@Slf4j
@RequiredArgsConstructor
public class ReservationsControllerImpl implements ReservationsController {

    private final ReservationServiceImpl reservationService;

    private final UserServiceImpl userService;

    @Override
    @GetMapping(value = "/reservations/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationDTO getById(@PathVariable Integer reservationId) {
        return new ReservationDTO(reservationService.find(reservationId));
    }

    @PreAuthorize("hasAnyRole('REGULAR_USER')")
    @GetMapping(value = "/my/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDTO> getMyReservations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        List<Reservation> reservationList = reservationService.findMyAllReservations(user);
        return reservationList.stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('SYSTEM_EMPLOYEE', 'SYSTEM_OWNER')")
    @GetMapping(value = "/reservations/today", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDTO> getAllToday() {
        LocalDate localDate = LocalDate.now();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        List<Reservation> reservationList = reservationService.findAllReservations(user, localDate);
        return reservationList.stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('SYSTEM_EMPLOYEE')")
    @GetMapping(value = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDTO> getAllForInterval(@RequestParam(name = "dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                                  @RequestParam(name = "dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        List<Reservation> reservationList = reservationService.findAllReservations(user, dateFrom, dateTo);
        return reservationList.stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @DeleteMapping(value = "/reservations/{reservationId}")
    public ResponseEntity<Void> cancel(@PathVariable Integer reservationId) {
        Reservation reservation = reservationService.find(reservationId);
        reservationService.cancelReservation(reservation);
        log.info("Canceled reservation {}.", reservation.getId());
        final HttpHeaders headers = RestUtil.createLocationHeaderNewUri("/reservations/{reservationId}", reservation.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
