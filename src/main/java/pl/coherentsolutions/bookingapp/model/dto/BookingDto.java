package pl.coherentsolutions.bookingapp.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingDto {

    private Integer id;
    private Integer userId;

    @FutureOrPresent
    private LocalDate  startDate;

    @FutureOrPresent
    private LocalDate endDate;

    @Positive
    private int totalPrice;

}
