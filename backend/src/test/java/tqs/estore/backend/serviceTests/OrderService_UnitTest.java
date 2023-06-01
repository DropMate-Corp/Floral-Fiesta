package tqs.estore.backend.serviceTests;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.estore.backend.connection.DropMateAPIClient;
import tqs.estore.backend.datamodel.*;
import tqs.estore.backend.exceptions.InvalidOrderException;
import tqs.estore.backend.exceptions.OrderNotFoundException;
import tqs.estore.backend.repositories.OrderItemRepository;
import tqs.estore.backend.repositories.OrderRepository;
import tqs.estore.backend.repositories.PlantRepository;
import tqs.estore.backend.repositories.UserRepository;
import tqs.estore.backend.services.OrderService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class OrderService_UnitTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PlantRepository plantRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private DropMateAPIClient dropMateAPIClient;

    @InjectMocks
    private OrderService orderService;
    private Order order;
    private OrderDTO orderDTO;
    private User user;
    private Plant plant;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);

        plant = new Plant();
        plant.setPlantId(1L);
        plant.setName("plant");
        plant.setPrice(10.0);
        plant.setPhoto("photo");
        plant.setCategory(null);
        plant.setDescription("description");

        order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setDeliveryDate(java.sql.Date.valueOf("2023-06-04"));
        order.setPickupCode("EXSV1043");
        order.setStatus(Status.IN_DELIVERY);
        order.setAcpID(1);
        order.setDescription("description");
        order.setTotalPrice(10.0);


        orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setPlantQuantityMap(Map.of(1L,1));
        orderDTO.setAcpID(1);
        orderDTO.setTotalPrice(10.0);

    }

    @AfterEach
    void tearDown() {
        plant = null;
        order = null;
        orderDTO = null;
        user = null;

    }

    @Test
    void whenCreateOrder_thenReturnOrder() throws URISyntaxException, ParseException, IOException, InvalidOrderException {
        when(dropMateAPIClient.postOrder(1, 1)).thenReturn((JSONObject) new JSONParser().parse(
                """
                        {
                            "delivery_date": "2023-06-04",
                            "pickup_code": "EXSV1043",
                            "status": "IN_DELIVERY"
                        }"""
        ));

        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        when(plantRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(plant));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());

        Order order = orderService.createOrder(orderDTO);

        assertThat(order).isNotNull();
        assertThat(order.getOrderId()).isEqualTo(1L);
        assertThat(order.getUser()).isEqualTo(user);
        assertThat(order.getDeliveryDate()).isEqualTo(java.sql.Date.valueOf(("2023-06-04")));
        assertThat(order.getPickupCode()).isEqualTo("EXSV1043");
        assertThat(order.getStatus()).isEqualTo(Status.IN_DELIVERY);
        assertThat(order.getAcpID()).isEqualTo(1);
        assertThat(order.getDescription()).isEqualTo("description");
        assertThat(order.getTotalPrice()).isEqualTo(10.0);

        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(userRepository, times(1)).findById(1L);
        verify(plantRepository, times(1)).findById(1L);
        verify(dropMateAPIClient, times(1)).postOrder(1, 1);

    }

    @Test
    void whenCreateOrderWithInvalidUser_thenThrowInvalidOrderException() throws URISyntaxException, ParseException, IOException {
            when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());


            assertThatThrownBy(() -> orderService.createOrder(orderDTO))
                    .isInstanceOf(InvalidOrderException.class)
                    .hasMessage("Invalid order.");

            verify(orderRepository, times(0)).save(any(Order.class));
            verify(userRepository, times(1)).findById(1L);
            verify(plantRepository, times(0)).findById(1L);
            verify(dropMateAPIClient, times(0)).postOrder(1, 1);
        }


    @Test
    void whenCreateOrderWithInvalidPlant_thenThrowInvalidOrderException() throws URISyntaxException, ParseException, IOException {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        when(plantRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(orderDTO))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("Invalid order.");

        verify(orderRepository, times(0)).save(any(Order.class));
        verify(userRepository, times(1)).findById(1L);
        verify(plantRepository, times(1)).findById(1L);
        verify(dropMateAPIClient, times(0)).postOrder(1, 1);
    }

    @Test
    void whenCreateInvalidOrder_thenThrowInvalidOrderException() throws URISyntaxException, ParseException, IOException {
        OrderDTO orderDTO = new OrderDTO();

        assertThatThrownBy(() -> orderService.createOrder(orderDTO))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("Invalid order.");

        verify(orderRepository, times(0)).save(any(Order.class));
        verify(userRepository, times(0)).findById(1L);
        verify(plantRepository, times(0)).findById(1L);
        verify(dropMateAPIClient, times(0)).postOrder(1, 1);
    }


    @Test
    void whenGetDeliveredOrders_thenReturnListOfOrders() throws URISyntaxException, ParseException, IOException {
        List<Order> orders = new ArrayList<>();
        Order order2 = new Order();
        order2.setOrderId(2L);
        order2.setTotalPrice(20.0);
        order2.setUser(user);
        order2.setPickupCode("4321");
        order2.setDescription("Test2");
        order2.setAcpID(2);
        order2.setStatus(Status.DELIVERED);
        order2.setDeliveryDate(Date.valueOf("2021-01-01"));
        order2.setPickupDate(Date.valueOf("2021-01-01"));

        orders.add(order2);

        when(orderRepository.findAllByUserUserIdAndStatus(1L, Status.DELIVERED)).thenReturn(orders);
        when(dropMateAPIClient.getParcelStatus(order2.getPickupCode())).thenReturn((JSONObject) new JSONParser().parse("{\"status\": \"DELIVERED\"}"));

        List<Order> ordersResponse = orderService.getDeliveredOrders(1L);

        assertThat(ordersResponse).isNotNull();
        assertThat(ordersResponse).hasSize(1);
        assertThat(ordersResponse.get(0)).isEqualTo(order2);

        verify(orderRepository, times(2)).findAllByUserUserIdAndStatus(1L, Status.DELIVERED);
    }

    @Test
    void whenGetDeliveredOrders_thenReturnEmptyList() throws URISyntaxException, ParseException, IOException {
        List<Order> orders = new ArrayList<>();

        when(orderRepository.findAllByUserUserIdAndStatus(1L, Status.DELIVERED)).thenReturn(orders);

        List<Order> ordersResponse = orderService.getDeliveredOrders(1L);

        assertThat(ordersResponse).isNotNull();
        assertThat(ordersResponse).hasSize(0);

        verify(orderRepository, times(1)).findAllByUserUserIdAndStatus(1L, Status.DELIVERED);
    }

    @Test
    void whenGetOrderById_thenReturnOrder() throws OrderNotFoundException {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(order));

        Order orderResponse = orderService.getOrderById(1L);

        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse).isEqualTo(order);

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void whenGetOrderById_thenThrowOrderNotFoundException() {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(1L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order not found.");

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void whenGetOngoingOrders_thenReturnListOfOrders() throws URISyntaxException, ParseException, IOException {
        List<Order> orders = new ArrayList<>();
        Order order2 = new Order();
        order2.setOrderId(2L);
        order2.setTotalPrice(20.0);
        order2.setUser(user);
        order2.setPickupCode("4321");
        order2.setDescription("Test2");
        order2.setAcpID(2);
        order2.setStatus(Status.IN_DELIVERY);
        order2.setDeliveryDate(Date.valueOf("2021-01-01"));
        order2.setPickupDate(Date.valueOf("2021-01-01"));

        orders.add(order2);

        when(orderRepository.findAllByUserUserIdAndStatus(1L, Status.IN_DELIVERY)).thenReturn(orders);
        when(orderRepository.findAllByUserUserIdAndStatus(1L, Status.WAITING_FOR_PICKUP)).thenReturn(new ArrayList<>());
        when(dropMateAPIClient.getParcelStatus(order2.getPickupCode())).thenReturn((JSONObject) new JSONParser().parse("{\"status\": \"IN_DELIVERY\"}"));

        List<Order> ordersResponse = orderService.getOnGoingOrders(1L);

        assertThat(ordersResponse).isNotNull();
        assertThat(ordersResponse).hasSize(1);
        assertThat(ordersResponse.get(0)).isEqualTo(order2);

        verify(orderRepository, times(2)).findAllByUserUserIdAndStatus(1L, Status.IN_DELIVERY);
        verify(orderRepository, times(2)).findAllByUserUserIdAndStatus(1L, Status.WAITING_FOR_PICKUP);
    }


}
