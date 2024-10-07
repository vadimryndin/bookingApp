package pl.coherentsolutions.bookingapp.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.coherentsolutions.bookingapp.model.dto.ApartmentDto;
import pl.coherentsolutions.bookingapp.model.dto.HotelDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;
import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-30T14:54:46+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class HotelMapperImpl implements HotelMapper {

    @Override
    public HotelDto hotelEntityToDto(HotelEntity hotelEntity) {
        if ( hotelEntity == null ) {
            return null;
        }

        HotelDto hotelDto = new HotelDto();

        hotelDto.setAdminId( hotelEntityAdminId( hotelEntity ) );
        hotelDto.setId( hotelEntity.getId() );
        hotelDto.setApartments( apartmentEntityListToApartmentDtoList( hotelEntity.getApartments() ) );
        hotelDto.setName( hotelEntity.getName() );
        hotelDto.setAddress( hotelEntity.getAddress() );
        hotelDto.setStarsCount( hotelEntity.getStarsCount() );

        return hotelDto;
    }

    @Override
    public HotelEntity hotelDtoToEntity(HotelDto hotelDto) {
        if ( hotelDto == null ) {
            return null;
        }

        HotelEntity hotelEntity = new HotelEntity();

        hotelEntity.setAdmin( mapUserIdToEntity( hotelDto.getAdminId() ) );
        hotelEntity.setId( hotelDto.getId() );
        hotelEntity.setName( hotelDto.getName() );
        hotelEntity.setAddress( hotelDto.getAddress() );
        hotelEntity.setStarsCount( hotelDto.getStarsCount() );
        hotelEntity.setApartments( apartmentDtoListToApartmentEntityList( hotelDto.getApartments() ) );

        return hotelEntity;
    }

    private Integer hotelEntityAdminId(HotelEntity hotelEntity) {
        if ( hotelEntity == null ) {
            return null;
        }
        UserEntity admin = hotelEntity.getAdmin();
        if ( admin == null ) {
            return null;
        }
        Integer id = admin.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected ApartmentDto apartmentEntityToApartmentDto(ApartmentEntity apartmentEntity) {
        if ( apartmentEntity == null ) {
            return null;
        }

        ApartmentDto apartmentDto = new ApartmentDto();

        apartmentDto.setId( apartmentEntity.getId() );
        apartmentDto.setName( apartmentEntity.getName() );
        apartmentDto.setAddress( apartmentEntity.getAddress() );
        apartmentDto.setPrice( apartmentEntity.getPrice() );

        return apartmentDto;
    }

    protected List<ApartmentDto> apartmentEntityListToApartmentDtoList(List<ApartmentEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<ApartmentDto> list1 = new ArrayList<ApartmentDto>( list.size() );
        for ( ApartmentEntity apartmentEntity : list ) {
            list1.add( apartmentEntityToApartmentDto( apartmentEntity ) );
        }

        return list1;
    }

    protected ApartmentEntity apartmentDtoToApartmentEntity(ApartmentDto apartmentDto) {
        if ( apartmentDto == null ) {
            return null;
        }

        ApartmentEntity apartmentEntity = new ApartmentEntity();

        apartmentEntity.setId( apartmentDto.getId() );
        apartmentEntity.setName( apartmentDto.getName() );
        apartmentEntity.setAddress( apartmentDto.getAddress() );
        apartmentEntity.setPrice( apartmentDto.getPrice() );

        return apartmentEntity;
    }

    protected List<ApartmentEntity> apartmentDtoListToApartmentEntityList(List<ApartmentDto> list) {
        if ( list == null ) {
            return null;
        }

        List<ApartmentEntity> list1 = new ArrayList<ApartmentEntity>( list.size() );
        for ( ApartmentDto apartmentDto : list ) {
            list1.add( apartmentDtoToApartmentEntity( apartmentDto ) );
        }

        return list1;
    }
}
