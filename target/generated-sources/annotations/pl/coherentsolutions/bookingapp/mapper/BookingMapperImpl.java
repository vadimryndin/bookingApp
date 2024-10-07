package pl.coherentsolutions.bookingapp.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.coherentsolutions.bookingapp.model.dto.BookingDto;
import pl.coherentsolutions.bookingapp.model.entity.BookingEntity;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-30T14:54:46+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingDto bookingEntityToDto(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }

        BookingDto bookingDto = new BookingDto();

        bookingDto.setUserId( bookingEntityUserId( bookingEntity ) );
        bookingDto.setId( bookingEntity.getId() );
        bookingDto.setStartDate( bookingEntity.getStartDate() );
        bookingDto.setEndDate( bookingEntity.getEndDate() );
        bookingDto.setTotalPrice( bookingEntity.getTotalPrice() );

        return bookingDto;
    }

    @Override
    public BookingEntity bookingDtoToEntity(BookingDto bookingDto) {
        if ( bookingDto == null ) {
            return null;
        }

        BookingEntity bookingEntity = new BookingEntity();

        bookingEntity.setUser( mapUserIdToEntity( bookingDto.getUserId() ) );
        bookingEntity.setId( bookingDto.getId() );
        bookingEntity.setTotalPrice( bookingDto.getTotalPrice() );
        bookingEntity.setStartDate( bookingDto.getStartDate() );
        bookingEntity.setEndDate( bookingDto.getEndDate() );

        return bookingEntity;
    }

    private Integer bookingEntityUserId(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }
        UserEntity user = bookingEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Integer id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
