package cz.cvut.fel.rsp.ReservationSystem.rest;

import cz.cvut.fel.rsp.ReservationSystem.dao.UserRepository;
import cz.cvut.fel.rsp.ReservationSystem.model.enums.UserType;
import cz.cvut.fel.rsp.ReservationSystem.model.user.User;
import cz.cvut.fel.rsp.ReservationSystem.response.JwtResponse;
import cz.cvut.fel.rsp.ReservationSystem.response.MessageResponse;
import cz.cvut.fel.rsp.ReservationSystem.security.jwt.JwtUtils;
import cz.cvut.fel.rsp.ReservationSystem.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/rest/v1/auth/")
@RestController
@Profile(value = {"!testprofile"})
@Deprecated
public class AuthControllerTest {
    @Autowired(required = false) // Required = false because of tests
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired(required = false) // Required = false because of tests
    PasswordEncoder encoder;
    @Autowired(required = false) // Required = false because of tests
    JwtUtils jwtUtils;

    @GetMapping("/exists/{username}")
    public boolean userExistsByUsername(@PathVariable String username) {
        return userRepository.existsByUsername(username);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestParam("username") String username,
                                              @RequestParam("password") String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

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

    @PostMapping(value = "/signup")
    public ResponseEntity<?> registerUser(@RequestParam("firstname") String firstname,
                                          @RequestParam("lastname") String lastname,
                                          @RequestParam("username") String username,
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("userType") String userType
    ) {
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));

        if (userType.equals("1")) user.setUserType(UserType.ROLE_SYSTEM_OWNER);
        if (userType.equals("2")) user.setUserType(UserType.ROLE_REGULAR_USER);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}