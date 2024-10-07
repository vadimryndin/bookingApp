package pl.coherentsolutions.bookingapp.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderDto {
    private Integer id;
    private Integer bookingId;
    private Integer apartmentId;

    @FutureOrPresent
    private LocalDate orderDate;

    @Positive
    private int totalPrice;
}
