package pl.coherentsolutions.bookingapp.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.coherentsolutions.bookingapp.model.dto.OrderDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.model.entity.BookingEntity;
import pl.coherentsolutions.bookingapp.model.entity.OrderEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-30T14:54:46+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto orderEntityToDto(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setBookingId( orderEntityBookingId( orderEntity ) );
        orderDto.setApartmentId( orderEntityApartmentId( orderEntity ) );
        orderDto.setId( orderEntity.getId() );
        orderDto.setOrderDate( orderEntity.getOrderDate() );
        orderDto.setTotalPrice( orderEntity.getTotalPrice() );

        return orderDto;
    }

    @Override
    public OrderEntity orderDtoToEnity(OrderDto orderDto) {
        if ( orderDto == null ) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setBooking( mapBookingIdToEntity( orderDto.getBookingId() ) );
        orderEntity.setApartment( mapApartmentIdToEntity( orderDto.getApartmentId() ) );
        orderEntity.setId( orderDto.getId() );
        orderEntity.setOrderDate( orderDto.getOrderDate() );
        orderEntity.setTotalPrice( orderDto.getTotalPrice() );

        return orderEntity;
    }

    private Integer orderEntityBookingId(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }
        BookingEntity booking = orderEntity.getBooking();
        if ( booking == null ) {
            return null;
        }
        Integer id = booking.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Integer orderEntityApartmentId(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }
        ApartmentEntity apartment = orderEntity.getApartment();
        if ( apartment == null ) {
            return null;
        }
        Integer id = apartment.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
