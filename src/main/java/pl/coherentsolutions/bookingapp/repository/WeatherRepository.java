package pl.coherentsolutions.bookingapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.coherentsolutions.bookingapp.model.entity.WeatherEntity;

import java.util.Optional;

public interface WeatherRepository extends CrudRepository<WeatherEntity, Integer> {
    Optional<WeatherEntity> findByCity(String city);
}
