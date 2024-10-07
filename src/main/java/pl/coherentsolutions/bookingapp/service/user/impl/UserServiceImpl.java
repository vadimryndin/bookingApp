package pl.coherentsolutions.bookingapp.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.coherentsolutions.bookingapp.model.UserRole;
import pl.coherentsolutions.bookingapp.model.dto.BookingDto;
import pl.coherentsolutions.bookingapp.model.dto.MostActiveBookingUser;
import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;
import pl.coherentsolutions.bookingapp.model.entity.OrderEntity;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;
import pl.coherentsolutions.bookingapp.repository.BookingRepository;
import pl.coherentsolutions.bookingapp.repository.OrderRepository;
import pl.coherentsolutions.bookingapp.repository.UserRepository;
import pl.coherentsolutions.bookingapp.service.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> getUserById(int id) {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers() {
        return (List<UserEntity>) this.userRepository.findAll();
    }

    @Override
    @Transactional
    public void saveOrUpdateUser(UserEntity user) {
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        this.userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MostActiveBookingUser getMostActiveUserForApartment(Integer apartmentId, int months) {
        LocalDate monthAgo = LocalDate.now().minusMonths(months);
        List<MostActiveBookingUser> activeUsers = bookingRepository.findMostActiveUserForApartment(apartmentId, monthAgo);

        if (activeUsers.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No bookings found for this apartment id " + apartmentId + " in the last " + months + " month");
        }

        return activeUsers.get(0);
    }

    @Override
    @Transactional
    public UserEntity findExistingUser(BookingDto bookingDto) {
        return getUserById(bookingDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User whit id " + bookingDto.getUserId() + " not found"));
    }

    @Override
    @Transactional
    public void assignUserRole(Integer userId, UserRole role) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with id " + userId + " not found"));

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void hasPermissionToCancelOrder(String username, Integer orderId) {
        boolean hasAdminPermission = isHotelAdmin(username, orderId) && isRoleAdmin(username);
        boolean hasUserPermission = isUsersOrder(username, orderId) && isRoleUser(username);
        boolean hasPermission = hasAdminPermission || hasUserPermission;

        if (!hasPermission) {
            throw new AccessDeniedException(String.format("User %s is not authorized to cancel the order with ID %s", username, orderId));
        }
    }

    @Override
    public void hasPermissionToEditHotel(String username, HotelEntity hotel) {
        boolean hasAdminPermission = isHotelAdmin(username, hotel) && isRoleUser(username);
        boolean canEditHotel = hasAdminPermission || isRoleAdmin(username);
        if (!canEditHotel) {
            throw new AccessDeniedException(String.format("User %s is not authorized to edit the hotel %s", username, hotel.getName()));
        }
    }

    private boolean isRoleUser(String username) {
        UserEntity user = findUserByUserName(username);

        return UserRole.ROLE_USER.equals(user.getRole());
    }

    private boolean isRoleAdmin(String username) {
        UserEntity user = findUserByUserName(username);

        return UserRole.ROLE_ADMIN.equals(user.getRole());
    }

    private boolean isHotelAdmin(String username, Integer orderId) {
        OrderEntity order = findOrderById(orderId);

        return order.getApartment().getHotel().getAdmin().getUsername().equals(username);
    }

    private boolean isHotelAdmin(String username, HotelEntity hotel) {
        return hotel.getAdmin().getUsername().equals(username);
    }

    private boolean isUsersOrder(String username, Integer orderId) {
        OrderEntity order = findOrderById(orderId);

        return order.getBooking().getUser().getUsername().equals(username);
    }

    private UserEntity findUserByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with name " + username + " not found"));
    }

    private OrderEntity findOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with id " + id + " not found"));
    }
}
