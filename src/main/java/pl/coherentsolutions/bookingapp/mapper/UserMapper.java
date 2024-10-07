package pl.coherentsolutions.bookingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.coherentsolutions.bookingapp.model.UserRole;
import pl.coherentsolutions.bookingapp.model.dto.UserDto;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role", target = "role", qualifiedByName = "stringToUserRole")
    UserDto userEntityToDto(UserEntity userEntity);

    @Mapping(source = "role", target = "role", qualifiedByName = "userRoleToString")
    UserEntity userDtoToEntity(UserDto userDto);

    @Named("stringToUserRole")
    default UserRole stringToUserRole(String role) {
        return UserRole.valueOf(role.toUpperCase());
    }

    @Named("userRoleToString")
    default String userRoleToString(UserRole role) {
        if (role == null) {
            // set default value
            role = UserRole.ROLE_USER;
        }
        return role.name();
    }
}
