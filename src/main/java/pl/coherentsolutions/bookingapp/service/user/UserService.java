package pl.coherentsolutions.bookingapp.service.user;

import pl.coherentsolutions.bookingapp.model.UserRole;
import pl.coherentsolutions.bookingapp.model.dto.BookingDto;
import pl.coherentsolutions.bookingapp.model.dto.MostActiveBookingUser;
import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserEntity> getUserById(int id);
    List<UserEntity> getAllUsers();
    void saveOrUpdateUser(UserEntity user);
    void deleteUser(int id);
    MostActiveBookingUser getMostActiveUserForApartment(Integer apartmentId, int months);
    UserEntity findExistingUser(BookingDto bookingDto);
    void assignUserRole(Integer userId, UserRole role);
    void hasPermissionToCancelOrder(String username, Integer id);
    void hasPermissionToEditHotel(String username, HotelEntity hotel);
}
