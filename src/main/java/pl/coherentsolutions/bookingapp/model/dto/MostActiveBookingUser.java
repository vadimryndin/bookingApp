package pl.coherentsolutions.bookingapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MostActiveBookingUser {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String apartmentName;
    private String apartmentAddress;
    private int apartmentPrice;
}
