package pl.coherentsolutions.bookingapp.service.booking.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.coherentsolutions.bookingapp.model.dto.OrderDto;
import pl.coherentsolutions.bookingapp.model.entity.BookingEntity;
import pl.coherentsolutions.bookingapp.repository.BookingRepository;
import pl.coherentsolutions.bookingapp.service.booking.BookingService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void saveOrUpdateBooking(BookingEntity booking) {
        bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingEntity> getBookingById(Integer id) {
        return bookingRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingEntity> getAllBookings() {
        return (List<BookingEntity>) bookingRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteBooking(Integer id) {
        bookingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookingEntity findExistingBooking(OrderDto orderDto) {
        return getBookingById(orderDto.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking whit id " + orderDto.getBookingId() + " not found"));
    }
}
