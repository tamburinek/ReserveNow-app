package cz.cvut.fel.rsp.ReservationSystem.rest.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.UserRepository;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Reservation;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.response.JwtResponse;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.CredentialsDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.UserDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.interfaces.UserController;
import cz.cvut.fel.rsp.ReservationSystem.rest.util.RestUtil;
import cz.cvut.fel.rsp.ReservationSystem.security.jwt.JwtUtils;
import cz.cvut.fel.rsp.ReservationSystem.security.services.UserDetailsImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.ReservationServiceImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/rest/v1/")
@RestController
@Profile(value = {"!testprofile"})
@Slf4j
public class UserControllerImpl implements UserController {

    @Autowired(required = false) // Required = false because of tests
    private AuthenticationManager authenticationManager;

    @Autowired
    private ReservationServiceImpl reservationService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired(required = false) // Required = false because of tests
    private PasswordEncoder encoder;

    @Autowired(required = false) // Required = false because of tests
    private JwtUtils jwtUtils;

    @Override
    @GetMapping("/users/me")
    public UserDTO getCurrentlyLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        UserDTO userDTO = new UserDTO(user);

        return userDTO;
    }

    @Override
    @GetMapping("/users/{username}")
    public UserDTO getByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        UserDTO userDTO = new UserDTO(user);
        return userDTO;
    }

    @Override
    @GetMapping("/users/id/{id}")
    public UserDTO getById(@PathVariable Integer id) {
        User user = userService.findById(id);
        UserDTO userDTO = new UserDTO(user);
        return userDTO;
    }

    @Override
    @GetMapping("/users/{username}/reservations")
    public List<ReservationDTO> getReservations(@PathVariable String username,
                                                @RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        User user = userService.findByUsername(username);
        List<Reservation> reservations = reservationService.findAllReservations(user, fromDate, toDate);
        return reservations.stream()
                .map(ReservationDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping("/users/signin")
    public ResponseEntity<?> authenticate(@RequestBody CredentialsDTO credentialsDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentialsDTO.getUsername(), credentialsDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles)
        );
    }

    @Override
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        userService.createUser(user);
        log.info("New user created {}", user);
        final HttpHeaders headers = RestUtil.createLocationHeaderNewUri("/users/{username}", user.getUsername());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping("/updateProfile")
    public void updateUser(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, @RequestParam(name = "email") String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(encoder.encode(password));

        userService.createUser(user);
        log.info("updated user: {}", user);
    }
}
