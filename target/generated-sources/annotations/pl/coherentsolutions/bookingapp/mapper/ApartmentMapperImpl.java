package pl.coherentsolutions.bookingapp.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.coherentsolutions.bookingapp.model.dto.ApartmentDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-30T14:54:46+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class ApartmentMapperImpl implements ApartmentMapper {

    @Override
    public ApartmentDto apartmentEntityToDto(ApartmentEntity apartmentEntity) {
        if ( apartmentEntity == null ) {
            return null;
        }

        ApartmentDto apartmentDto = new ApartmentDto();

        apartmentDto.setHotelId( apartmentEntityHotelId( apartmentEntity ) );
        apartmentDto.setId( apartmentEntity.getId() );
        apartmentDto.setName( apartmentEntity.getName() );
        apartmentDto.setAddress( apartmentEntity.getAddress() );
        apartmentDto.setPrice( apartmentEntity.getPrice() );

        return apartmentDto;
    }

    @Override
    public ApartmentEntity apartmentDtoToEntity(ApartmentDto apartmentDto) {
        if ( apartmentDto == null ) {
            return null;
        }

        ApartmentEntity apartmentEntity = new ApartmentEntity();

        apartmentEntity.setHotel( map( apartmentDto.getHotelId() ) );
        apartmentEntity.setId( apartmentDto.getId() );
        apartmentEntity.setName( apartmentDto.getName() );
        apartmentEntity.setAddress( apartmentDto.getAddress() );
        apartmentEntity.setPrice( apartmentDto.getPrice() );

        return apartmentEntity;
    }

    private Integer apartmentEntityHotelId(ApartmentEntity apartmentEntity) {
        if ( apartmentEntity == null ) {
            return null;
        }
        HotelEntity hotel = apartmentEntity.getHotel();
        if ( hotel == null ) {
            return null;
        }
        Integer id = hotel.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
