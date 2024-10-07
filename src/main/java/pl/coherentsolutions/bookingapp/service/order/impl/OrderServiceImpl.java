package pl.coherentsolutions.bookingapp.service.order.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coherentsolutions.bookingapp.model.entity.OrderEntity;
import pl.coherentsolutions.bookingapp.repository.OrderRepository;
import pl.coherentsolutions.bookingapp.service.order.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void saveOrUpdateOrder(OrderEntity order) {
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderEntity> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderEntity> getAllOrders() {
        return (List<OrderEntity>) orderRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}
