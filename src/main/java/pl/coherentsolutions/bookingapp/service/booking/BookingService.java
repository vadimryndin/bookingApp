package pl.coherentsolutions.bookingapp.service.booking;

import pl.coherentsolutions.bookingapp.model.dto.OrderDto;
import pl.coherentsolutions.bookingapp.model.entity.BookingEntity;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    void saveOrUpdateBooking(BookingEntity booking);
    Optional<BookingEntity> getBookingById(Integer id);
    List<BookingEntity> getAllBookings();
    void deleteBooking(Integer id);
    BookingEntity findExistingBooking(OrderDto orderDto);
}
