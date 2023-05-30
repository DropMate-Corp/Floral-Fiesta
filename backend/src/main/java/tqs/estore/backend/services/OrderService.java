package tqs.estore.backend.services;

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

    public Order createOrder(Order order) {
        return null;
    }

    public Order getOrderById(Long orderId) {
        return null;
    }

    public List<Order> getOnGoingOrders(Long userId) {
        return null;
    }

    public List<Order> getDeliveredOrders(Long userId) {
        return null;
    }
}
