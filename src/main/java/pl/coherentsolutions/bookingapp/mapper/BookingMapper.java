package pl.coherentsolutions.bookingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.coherentsolutions.bookingapp.model.dto.BookingDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.model.entity.BookingEntity;
import pl.coherentsolutions.bookingapp.model.entity.OrderEntity;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "userId", source = "user.id")
    BookingDto bookingEntityToDto(BookingEntity bookingEntity);

    @Mapping(target = "user", source = "userId")
    BookingEntity bookingDtoToEntity(BookingDto bookingDto);

    default OrderEntity mapApartmentIdToEntity(Integer orderId) {
        if (orderId == null) {
            return null;
        }
        OrderEntity order = new OrderEntity();
        order.setId(orderId);
        return order;
    }

    default UserEntity mapUserIdToEntity(Integer userId) {
        if (userId == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(userId);
        return user;
    }
}
