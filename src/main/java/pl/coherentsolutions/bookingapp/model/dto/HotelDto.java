package pl.coherentsolutions.bookingapp.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    private Integer id;
    private List<ApartmentDto> apartments;
    private Integer adminId;

    @NotBlank(message = "Hotel name should not be empty")
    private String name;

    @NotBlank(message = "Address should not be empty")
    private String address;

    @NotBlank(message = "Stars count should not be empty")
    @Min(0)
    @Max(5)
    private int starsCount;
}
