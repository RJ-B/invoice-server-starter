package cz.itnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    // U Google účtu bude null
    private String password;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ========== Osobní údaje ==========
    private String firstName;
    private String lastName;
    private String phone;

    // ========== Google OAuth2 ==========
    private boolean oauthUser = false;
    private String googleId;
    private String profilePicture;
}
