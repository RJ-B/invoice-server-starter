package cz.itnetwork.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginDTO {
    private String idToken;   // Token, který pošle frontend (Google ID token)
}
