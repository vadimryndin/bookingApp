package pl.coherentsolutions.bookingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.coherentsolutions.bookingapp.model.dto.OrderDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.model.entity.BookingEntity;
import pl.coherentsolutions.bookingapp.model.entity.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "apartmentId", source = "apartment.id")
    OrderDto orderEntityToDto(OrderEntity orderEntity);

    @Mapping(target = "booking", source = "bookingId")
    @Mapping(target = "apartment", source = "apartmentId")
    OrderEntity orderDtoToEnity(OrderDto orderDto);

    default BookingEntity mapBookingIdToEntity(Integer bookingId) {
        if (bookingId == null) {
            return null;
        }
        BookingEntity booking = new BookingEntity();
        booking.setId(bookingId);
        return booking;
    }

    default ApartmentEntity mapApartmentIdToEntity(Integer apartmentId) {
        if (apartmentId == null) {
            return null;
        }
        ApartmentEntity apartment = new ApartmentEntity();
        apartment.setId(apartmentId);
        return apartment;
    }
}
