package cz.itnetwork.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthDTO {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
}
