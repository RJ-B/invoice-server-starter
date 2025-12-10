package cz.itnetwork.service.User;

import cz.itnetwork.dto.user.UserAuthDTO;
import cz.itnetwork.entity.User;

public interface UserService {

    // Lokální registrace
    User register(UserAuthDTO dto);

    // Google registrace/login
    User registerOrLoginGoogle(String googleId, String email, String name, String picture);

    User findByEmail(String email);
}
