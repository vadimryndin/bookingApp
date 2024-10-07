package pl.coherentsolutions.bookingapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ApartmentDto {
    private Integer id;
    private Integer hotelId;

    @NotBlank(message = "Apartment name should not be empty")
    private String name;

    @NotBlank(message = "Address should not be empty")
    private String address;

    @Positive(message = "Enter a valid price")
    private int price;
}
