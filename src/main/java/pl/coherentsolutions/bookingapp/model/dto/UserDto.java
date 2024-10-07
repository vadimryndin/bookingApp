package pl.coherentsolutions.bookingapp.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.coherentsolutions.bookingapp.model.UserRole;

@Getter
@RequiredArgsConstructor
@ToString
public class UserDto {

    @Setter
    private Integer id;

    @NotBlank(message = "First name should not be empty")
    private final String firstName;

    @NotBlank(message = "Last name should not be empty")
    private final String lastName;

    @NotBlank(message = "Username should not be empty")
    private final String username;

    @NotBlank(message = "Password should not be empty")
    private final String password;

    @Email(message = "Email should be valid")
    private final String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    private final String phoneNumber;

    @Setter
    private UserRole role;

    @NotBlank(message = "City should not be empty")
    private final String city;
}
