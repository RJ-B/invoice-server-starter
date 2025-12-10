package cz.itnetwork.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleAuthDTO {
    private String googleId;
    private String email;
    private String fullName;
    private String picture;
}
