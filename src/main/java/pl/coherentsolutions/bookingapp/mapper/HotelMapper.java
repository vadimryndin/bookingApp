package pl.coherentsolutions.bookingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.coherentsolutions.bookingapp.model.dto.HotelDto;
import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    @Mapping(target = "adminId", source = "admin.id")
    HotelDto hotelEntityToDto(HotelEntity hotelEntity);

    @Mapping(target = "admin", source = "adminId")
    HotelEntity hotelDtoToEntity(HotelDto hotelDto);

    default UserEntity mapUserIdToEntity(Integer userId) {
        if (userId == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(userId);
        return user;
    }
}
