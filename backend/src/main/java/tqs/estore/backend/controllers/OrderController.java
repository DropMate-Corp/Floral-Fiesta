package tqs.estore.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.estore.backend.datamodel.Order;
import tqs.estore.backend.services.OrderService;

import java.util.List;

@RestController
@RequestMapping("floralfiesta/order")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return null;
    }

    @GetMapping("/ongoing/{userId}")
    public ResponseEntity<List<Order>> getOnGoingOrders(@PathVariable Long userId){
        return null;
    }

    @GetMapping("/delivered/{userId}")
    public ResponseEntity<List<Order>> getDeliveredOrders(@PathVariable Long userId){
        return null;
    }

}
