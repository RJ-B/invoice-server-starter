package cz.itnetwork.dto.user;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean oauthUser;
    private String profilePicture;
}
