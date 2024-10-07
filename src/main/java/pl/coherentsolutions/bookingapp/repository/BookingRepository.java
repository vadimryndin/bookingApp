package pl.coherentsolutions.bookingapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.coherentsolutions.bookingapp.model.dto.MostActiveBookingUser;
import pl.coherentsolutions.bookingapp.model.entity.BookingEntity;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends CrudRepository<BookingEntity, Integer> {
    @Query("SELECT NEW pl.coherentsolutions.bookingapp.model.dto.MostActiveBookingUser(u.firstName, u.lastName, u.email, u.phoneNumber, a.name, a.address, a.price) " +
            "FROM OrderEntity o " +
            "JOIN o.booking b " +
            "JOIN b.user u " +
            "JOIN o.apartment a " +
            "WHERE a.id = :apartmentId AND b.startDate >= :startDate " +
            "GROUP BY u.id, u.firstName, u.lastName, u.email, u.phoneNumber, a.name, a.address, a.price " +
            "ORDER BY COUNT(b.id) DESC")
    List<MostActiveBookingUser> findMostActiveUserForApartment(@Param("apartmentId") Integer apartmentId, @Param("startDate") LocalDate startDate);
}
