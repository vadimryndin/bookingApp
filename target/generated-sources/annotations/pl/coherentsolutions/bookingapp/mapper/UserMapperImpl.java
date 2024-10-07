package pl.coherentsolutions.bookingapp.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.coherentsolutions.bookingapp.model.UserRole;
import pl.coherentsolutions.bookingapp.model.dto.UserDto;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-30T14:54:46+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto userEntityToDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        String firstName = null;
        String lastName = null;
        String username = null;
        String password = null;
        String email = null;
        String phoneNumber = null;
        String city = null;

        firstName = userEntity.getFirstName();
        lastName = userEntity.getLastName();
        username = userEntity.getUsername();
        password = userEntity.getPassword();
        email = userEntity.getEmail();
        phoneNumber = userEntity.getPhoneNumber();
        city = userEntity.getCity();

        UserDto userDto = new UserDto( firstName, lastName, username, password, email, phoneNumber, city );

        if ( userEntity.getRole() != null ) {
            userDto.setRole( stringToUserRole( userEntity.getRole().name() ) );
        }
        userDto.setId( userEntity.getId() );

        return userDto;
    }

    @Override
    public UserEntity userDtoToEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        if ( userDto.getRole() != null ) {
            userEntity.setRole( Enum.valueOf( UserRole.class, userRoleToString( userDto.getRole() ) ) );
        }
        userEntity.setId( userDto.getId() );
        userEntity.setFirstName( userDto.getFirstName() );
        userEntity.setLastName( userDto.getLastName() );
        userEntity.setUsername( userDto.getUsername() );
        userEntity.setPassword( userDto.getPassword() );
        userEntity.setEmail( userDto.getEmail() );
        userEntity.setPhoneNumber( userDto.getPhoneNumber() );
        userEntity.setCity( userDto.getCity() );

        return userEntity;
    }
}
