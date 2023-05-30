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
import tqs.estore.backend.repositories.OrderRepository;
import tqs.estore.backend.repositories.PlantRepository;
import tqs.estore.backend.repositories.UserRepository;
import tqs.estore.backend.services.OrderService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
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

        Map<Plant, Integer> plantQuantityMap = Map.of(plant, 1);

        order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setDeliveryDate(new java.sql.Date((new Date("2023/06/04")).getTime()));
        order.setPickupCode("EXSV1043");
        order.setStatus(Status.IN_DELIVERY);
        order.setAcpID(1);
        order.setDescription("description");
        order.setTotalPrice(10.0);
        order.setPlantQuantityMap(plantQuantityMap);


        orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setPlantQuantityMap(Map.of(1L,1));
        orderDTO.setAcpID(1);

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

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        when(plantRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(plant));

        Order order = orderService.createOrder(orderDTO);

        assertThat(order).isNotNull();
        assertThat(order.getOrderId()).isEqualTo(1L);
        assertThat(order.getUser()).isEqualTo(user);
        assertThat(order.getDeliveryDate()).isEqualTo(new java.sql.Date(new Date("2023-06-04").getTime()));
        assertThat(order.getPickupCode()).isEqualTo("EXSV1043");
        assertThat(order.getStatus()).isEqualTo(Status.IN_DELIVERY);
        assertThat(order.getAcpID()).isEqualTo(1);
        assertThat(order.getDescription()).isEqualTo("description");
        assertThat(order.getTotalPrice()).isEqualTo(10.0);
        assertThat(order.getPlantQuantityMap()).isEqualTo(Map.of(plant, 1));

        verify(orderRepository, times(1)).save(any(Order.class));
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
    void whenCreateOrderWithInvalidAcp_thenThrowInvalidOrderException() throws URISyntaxException, ParseException, IOException {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        when(plantRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(plant));
        when(dropMateAPIClient.postOrder(1, 1)).thenReturn((JSONObject) new JSONParser().parse(
                """
                        {
                            "delivery_date": "2023-06-04",
                            "pickup_code": "EXSV1043",
                            "status": "IN_DELIVERY"
                        }"""
        ));

       assertThatThrownBy(() -> orderService.createOrder(orderDTO))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("Invalid order.");

        verify(orderRepository, times(0)).save(any(Order.class));
        verify(userRepository, times(1)).findById(1L);
        verify(plantRepository, times(1)).findById(1L);
        verify(dropMateAPIClient, times(1)).postOrder(1, 1);
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
}
