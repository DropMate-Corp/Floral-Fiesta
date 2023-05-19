package tqs.estore.backend.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.estore.backend.datamodel.Order;
import tqs.estore.backend.repositories.OrderRepository;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<Order> createOrder(Order order) {
        return null;
    }

    public ResponseEntity<Order> getOrderById(Long orderId) {
        return null;
    }

    public ResponseEntity<List<Order>> getOnGoingOrders(Long userId) {
        return null;
    }

    public ResponseEntity<List<Order>> getDeliveredOrders(Long userId) {
        return null;
    }
}
