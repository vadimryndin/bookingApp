package pl.coherentsolutions.bookingapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.coherentsolutions.bookingapp.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
}
