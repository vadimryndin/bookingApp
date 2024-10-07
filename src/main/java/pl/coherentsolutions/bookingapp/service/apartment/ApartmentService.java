package pl.coherentsolutions.bookingapp.service.apartment;

import pl.coherentsolutions.bookingapp.model.dto.OrderDto;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;

import java.util.List;
import java.util.Optional;

public interface ApartmentService {
    void saveOrUpdateApartment(ApartmentEntity apartmentDto);
    Optional<ApartmentEntity> getApartmentById(Integer id);
    List<ApartmentEntity> getAllApartments();
    void deleteApartment(Integer id);
    ApartmentEntity findExistingApartment(OrderDto orderDto);
}
