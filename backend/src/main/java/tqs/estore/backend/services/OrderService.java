package tqs.estore.backend.services;

import org.springframework.stereotype.Service;
import tqs.estore.backend.connection.DropMateAPIClient;
import tqs.estore.backend.datamodel.Order;
import tqs.estore.backend.datamodel.OrderDTO;
import tqs.estore.backend.exceptions.InvalidOrderException;
import tqs.estore.backend.repositories.OrderRepository;
import tqs.estore.backend.repositories.PlantRepository;
import tqs.estore.backend.repositories.UserRepository;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
    private DropMateAPIClient dropMateAPIClient;


    public OrderService(OrderRepository orderRepository, DropMateAPIClient dropMateAPIClient,
                        UserRepository userRepository, PlantRepository plantRepository) {
        this.orderRepository = orderRepository;
        this.dropMateAPIClient = dropMateAPIClient;
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;

    }

    public Order createOrder(OrderDTO order) throws InvalidOrderException {
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
