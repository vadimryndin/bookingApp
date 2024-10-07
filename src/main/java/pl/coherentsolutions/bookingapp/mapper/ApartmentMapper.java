package pl.coherentsolutions.bookingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.coherentsolutions.bookingapp.model.dto.ApartmentDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;

@Mapper(componentModel = "spring")
public interface ApartmentMapper {
    @Mapping(source = "hotel.id", target = "hotelId")
    ApartmentDto apartmentEntityToDto(ApartmentEntity apartmentEntity);

    @Mapping(target = "hotel", source = "hotelId")
    ApartmentEntity apartmentDtoToEntity(ApartmentDto apartmentDto);

    default HotelEntity map(Integer hotelId) {
        if (hotelId == null) {
            return null;
        }
        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        return hotel;
    }
}
