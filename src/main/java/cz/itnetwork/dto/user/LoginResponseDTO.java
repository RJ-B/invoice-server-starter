package cz.itnetwork.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String token;
    private String email;
    private String role;

    private String firstName;
    private String lastName;
    private String profilePicture;
}
