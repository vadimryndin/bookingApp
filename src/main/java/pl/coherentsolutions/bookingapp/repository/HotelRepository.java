package pl.coherentsolutions.bookingapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.coherentsolutions.bookingapp.model.entity.HotelEntity;

@Repository
public interface HotelRepository extends CrudRepository<HotelEntity, Integer> {
}
