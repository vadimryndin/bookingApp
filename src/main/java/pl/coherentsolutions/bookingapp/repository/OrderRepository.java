package pl.coherentsolutions.bookingapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.coherentsolutions.bookingapp.model.entity.OrderEntity;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {
}
