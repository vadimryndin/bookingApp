package pl.coherentsolutions.bookingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import pl.coherentsolutions.bookingapp.mapper.BookingMapper;
import pl.coherentsolutions.bookingapp.model.dto.BookingDto;
import pl.coherentsolutions.bookingapp.model.entity.BookingEntity;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;
import pl.coherentsolutions.bookingapp.service.booking.BookingService;
import pl.coherentsolutions.bookingapp.service.user.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/bookingApp/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final BookingMapper bookingMapper;

    @Operation(summary = "Find booking by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the booking",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookingEntity.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content)
    })
    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public BookingDto findBookingById(@PathVariable Integer id) {
        return bookingMapper.bookingEntityToDto(bookingService.getBookingById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking with id " + id + " not found")));
    }

    @Operation(summary = "Find all bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the bookings",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookingEntity.class)) })
    })
    @GetMapping("/")
    @Secured({"ROLE_ADMIN"})
    public List<BookingDto> findAllBookings() {
        return bookingService.getAllBookings().stream().map(bookingMapper::bookingEntityToDto).toList();
    }

    @Operation(summary = "Create a new booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void createBooking(@Valid @RequestBody BookingDto bookingDto) {
        UserEntity user = userService.findExistingUser(bookingDto);

        BookingEntity booking = bookingMapper.bookingDtoToEntity(bookingDto);
        booking.setUser(user);

        bookingService.saveOrUpdateBooking(booking);
    }

    @Operation(summary = "Update an existing booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PutMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void updateBooking(@PathVariable Integer id, @Valid @RequestBody BookingDto bookingDto) {
        bookingDto.setId(id);
        bookingService.saveOrUpdateBooking(prepareBookingToUpdate(bookingDto));
    }

    @Operation(summary = "Delete a booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public void deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
    }

    private BookingEntity prepareBookingToUpdate(BookingDto bookingDto) {
        Optional<BookingEntity> existingBookingOptional = bookingService.getBookingById(bookingDto.getId());

        if (existingBookingOptional.isPresent()) {
            BookingEntity entityToUpdate = existingBookingOptional.get();
            if (bookingDto.getStartDate() != null) {
                entityToUpdate.setStartDate(bookingDto.getStartDate());
            }
            if (bookingDto.getEndDate() != null) {
                entityToUpdate.setEndDate(bookingDto.getEndDate());
            }
            if (bookingDto.getUserId() != null) {
                entityToUpdate.setUser(userService.findExistingUser(bookingDto));
            }
            entityToUpdate.setTotalPrice(bookingDto.getTotalPrice());

            return entityToUpdate;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking whit id " + bookingDto.getId() + " does not exist");
        }
    }
}
