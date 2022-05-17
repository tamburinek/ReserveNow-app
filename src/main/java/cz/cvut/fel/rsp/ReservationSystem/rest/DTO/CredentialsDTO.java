package cz.cvut.fel.rsp.ReservationSystem.rest.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CredentialsDTO {
    private String username;

    private String password;

    public CredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
