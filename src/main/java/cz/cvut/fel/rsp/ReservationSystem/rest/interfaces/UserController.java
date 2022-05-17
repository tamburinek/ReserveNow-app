package cz.cvut.fel.rsp.ReservationSystem.rest.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.CredentialsDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.ReservationDTO;
import cz.cvut.fel.rsp.ReservationSystem.rest.DTO.UserDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

// TODO jeste neni hotove, poresime potom
// Na usera ktery to dela se
public interface UserController {
    public UserDTO getByUsername(String username);

    public UserDTO getById(Integer id);

    public ResponseEntity<?> authenticate(CredentialsDTO credentialsDTO);

    public ResponseEntity<?> createUser(UserDTO userDTO);

    public void updateUser(String username, String password, String email);

    public List<ReservationDTO> getReservations(String username, LocalDate fromDate, LocalDate toDate);

    public UserDTO getCurrentlyLoggedInUser();
}
