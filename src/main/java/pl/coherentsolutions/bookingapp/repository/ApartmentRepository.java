package pl.coherentsolutions.bookingapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.coherentsolutions.bookingapp.model.entity.ApartmentEntity;

public interface ApartmentRepository extends CrudRepository<ApartmentEntity, Integer> {
}
