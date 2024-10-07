package pl.coherentsolutions.bookingapp.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
    private Integer id;

    @NotBlank(message = "City should not be empty")
    private String city;

    @NotBlank(message = "Weather data should not be empty")
    private String weatherData;

    @NotBlank(message = "Date should not be empty")
    private LocalDateTime lastUpdated;
}
