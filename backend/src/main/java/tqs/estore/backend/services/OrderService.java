package tqs.estore.backend.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import tqs.estore.backend.connection.DropMateAPIClient;
import tqs.estore.backend.datamodel.*;
import tqs.estore.backend.exceptions.InvalidOrderException;
import tqs.estore.backend.exceptions.OrderNotFoundException;
import tqs.estore.backend.repositories.OrderItemRepository;
import tqs.estore.backend.repositories.OrderRepository;
import tqs.estore.backend.repositories.PlantRepository;
import tqs.estore.backend.repositories.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
    private final OrderItemRepository orderItemRepository;
    private final DropMateAPIClient dropMateAPIClient;


    public OrderService(OrderRepository orderRepository, DropMateAPIClient dropMateAPIClient,
                        UserRepository userRepository, PlantRepository plantRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.dropMateAPIClient = dropMateAPIClient;
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order createOrder(OrderDTO order) throws InvalidOrderException, URISyntaxException, ParseException, IOException {
        Double totalPrice = order.getTotalPrice();
        Integer acpID = order.getAcpID();
        Long userId = order.getUserId();
        Map<Long, Integer> plantQuantityMap = order.getPlantQuantityMap();

        if (totalPrice == null || acpID == null || userId == null || plantQuantityMap == null) {
            throw new InvalidOrderException();
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new InvalidOrderException();
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : plantQuantityMap.entrySet()) {
            Plant plant = plantRepository.findById(entry.getKey()).orElse(null);
            if (plant == null) {
                throw new InvalidOrderException();
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setPlant(plant);
            orderItem.setQuantity(entry.getValue());
            orderItems.add(orderItem);
        }

        JSONObject response = dropMateAPIClient.postOrder(acpID, 1);

        Order newOrder = new Order();
        newOrder.setTotalPrice(totalPrice);
        newOrder.setAcpID(acpID);
        newOrder.setUser(user);
        newOrder.setDescription("Order by " + user.getName() + " on " + new Date());
        newOrder.setDeliveryDate(java.sql.Date.valueOf((String) response.get("delivery_date")));
        newOrder.setPickupCode((String) response.get("pickup_code"));
        newOrder.setStatus((Status.valueOf((String) response.get("status"))));

        newOrder = orderRepository.saveAndFlush(newOrder);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(newOrder);
            orderItemRepository.save(orderItem);
        }

        return newOrder;
    }

    public Order getOrderById(Long orderId) throws OrderNotFoundException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
        throw new OrderNotFoundException();
    }

    public List<Order> getOnGoingOrders(Long userId) throws URISyntaxException, ParseException, IOException {
        List<Order> orders = orderRepository.findAllByUserUserIdAndStatus(userId, Status.IN_DELIVERY);
        orders.addAll(orderRepository.findAllByUserUserIdAndStatus(userId, Status.WAITING_FOR_PICKUP));

        for (Order order: orders){
            JSONObject reponse = dropMateAPIClient.getParcelStatus(order.getPickupCode());
            order.setStatus(Status.valueOf((String) reponse.get("status")));
            String pickupDate = (String) reponse.get("pickup_date");
            if (pickupDate != null) {
                order.setPickupDate(java.sql.Date.valueOf(pickupDate));
            }
            else {
                order.setPickupDate(null);
            }

            String deliveryDate = (String) reponse.get("delivery_date");
            if (deliveryDate != null) {
                order.setDeliveryDate(java.sql.Date.valueOf(deliveryDate));
            }
            else {
                order.setDeliveryDate(null);
            }
            orderRepository.saveAndFlush(order);
        }

        List<Order> returnOrders = orderRepository.findAllByUserUserIdAndStatus(userId, Status.IN_DELIVERY);
        returnOrders.addAll(orderRepository.findAllByUserUserIdAndStatus(userId, Status.WAITING_FOR_PICKUP));
        return returnOrders;
    }

    public List<Order> getDeliveredOrders(Long userId) throws URISyntaxException, ParseException, IOException {
        List<Order> orders = orderRepository.findAllByUserUserIdAndStatus(userId, Status.DELIVERED);

        for (Order order: orders){
            JSONObject reponse = dropMateAPIClient.getParcelStatus(order.getPickupCode());
            order.setStatus(Status.valueOf((String) reponse.get("status")));

            String pickupDate = (String) reponse.get("pickup_date");
            if (pickupDate != null) {
                order.setPickupDate(java.sql.Date.valueOf(pickupDate));
            }
            else {
                order.setPickupDate(null);
            }

            String deliveryDate = (String) reponse.get("delivery_date");
            if (deliveryDate != null) {
                order.setDeliveryDate(java.sql.Date.valueOf(deliveryDate));
            }
            else {
                order.setDeliveryDate(null);
            }

            orderRepository.saveAndFlush(order);
        }

        return orderRepository.findAllByUserUserIdAndStatus(userId, Status.DELIVERED);
    }

}
