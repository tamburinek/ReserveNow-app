package cz.cvut.fel.rsp.ReservationSystem.rest.impl;


import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.SlotDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.interfaces.SlotController;
import cz.cvut.fel.rsp.ReservationSystem.rest.util.RestUtil;
import cz.cvut.fel.rsp.ReservationSystem.security.services.UserDetailsImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationService;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.ReservationSlotService;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/")
@Slf4j
@RequiredArgsConstructor
public class SlotControllerImpl implements SlotController {

    private final ReservationSlotService reservationSlotService;
    private final ReservationService reservationService;
    private final UserService userService;

    @Override
    @GetMapping(value = "/slots/{slot_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SlotDTO getSlotById(@PathVariable Integer slot_id) {
        return reservationSlotService.find(slot_id).toDTO();
    }

    @Override
    @PostMapping(value = "/slots/{slot_id}")
    public ResponseEntity<Void> createReservation(@PathVariable Integer slot_id, @RequestBody ReservationDTO reservationDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        Reservation reservation = new Reservation(reservationDTO, userService.findById(user.getId()));

        reservation.setReservationSlot(reservationSlotService.find(slot_id));
        reservationService.createReservation(reservation);
        log.info("Created reservation {} for slot with id {}", reservation, slot_id);
        final HttpHeaders headers = RestUtil.createLocationHeaderNewUri("/slots/{slot_id}/reservation", reservation.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    @PostMapping(value = "/slots/{slot_id}/admin")
    public ResponseEntity<Void> createReservationAdmin(@PathVariable Integer slot_id, @RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation(reservationDTO, userService.findByUsername(reservationDTO.getUsername()));
        reservation.setReservationSlot(reservationSlotService.find(slot_id));
        reservationService.createReservation(reservation);
        log.info("Created reservation {} for slot with id {}", reservation, slot_id);
        final HttpHeaders headers = RestUtil.createLocationHeaderNewUri("/slots/{slot_id}/reservation", reservation.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
