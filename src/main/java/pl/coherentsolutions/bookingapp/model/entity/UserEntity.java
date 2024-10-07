package pl.coherentsolutions.bookingapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.coherentsolutions.bookingapp.model.UserRole;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NonNull
    private String firstName;

    @Column
    @NonNull
    private String lastName;

    @Column(unique = true)
    @NonNull
    private String username;

    @Column
    @NonNull
    private String password;

    @Column(unique = true)
    @Email
    @NonNull
    private String email;

    @Column
    @NonNull
    private String phoneNumber;

    @Column
    @NonNull
    private UserRole role;

    @Column
    @NonNull
    private String city;
}
