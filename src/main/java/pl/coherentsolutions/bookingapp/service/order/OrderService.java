package pl.coherentsolutions.bookingapp.service.order;

import pl.coherentsolutions.bookingapp.model.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void saveOrUpdateOrder(OrderEntity order);
    Optional<OrderEntity> getOrderById(Integer id);
    List<OrderEntity> getAllOrders();
    void deleteOrder(Integer id);
}
