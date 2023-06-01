package tqs.estore.backend.controllers;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.estore.backend.datamodel.Order;
import tqs.estore.backend.datamodel.OrderDTO;
import tqs.estore.backend.exceptions.InvalidOrderException;
import tqs.estore.backend.exceptions.OrderNotFoundException;
import tqs.estore.backend.services.OrderService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("floralfiesta/order")
@CrossOrigin(origins = "https://dropmate-corp.github.io/Floral-Fiesta-UI/")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO order) throws InvalidOrderException, URISyntaxException, ParseException, IOException {
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @GetMapping("/ongoing/{userId}")
    public ResponseEntity<List<Order>> getOnGoingOrders(@PathVariable Long userId) throws URISyntaxException, ParseException, IOException {
        return new ResponseEntity<>(orderService.getOnGoingOrders(userId), HttpStatus.OK);
    }

    @GetMapping("/delivered/{userId}")
    public ResponseEntity<List<Order>> getDeliveredOrders(@PathVariable Long userId) throws URISyntaxException, ParseException, IOException {
        return new ResponseEntity<>(orderService.getDeliveredOrders(userId), HttpStatus.OK);
    }

}
