package tqs.estore.backend.boundaryTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.estore.backend.controllers.OrderController;
import tqs.estore.backend.datamodel.*;
import tqs.estore.backend.exceptions.InvalidOrderException;
import tqs.estore.backend.services.OrderService;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = OrderController.class)
public class OrderController_withMockServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private Order order;
    private User user;
    private Plant plant;
    private Map<Plant, Integer> plants_quantity;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setName("Test");
        user.setEmail("test@email.com");
        user.setPassword("test");
        user.setPhoneNumber(123456789);
        user.setAddress("Test Street");

        order = new Order();

        order.setOrderId(1L);
        order.setTotalPrice(10.0);
        order.setUser(user);
        order.setPickupCode("1234");
        order.setDescription("Test");
        order.setAcpID(1);
        order.setStatus(Status.WAITING_FOR_PICKUP);
        order.setDeliveryDate(Date.valueOf("2021-01-01"));
        order.setPickupDate(Date.valueOf("2021-01-01"));

        plants_quantity = new HashMap<>();
        plant = new Plant();
        plant.setPlantId(1L);
        plant.setName("Orchid");
        plant.setPrice(12.0);
        plant.setPhoto("orchid.jpg");
        plant.setDescription("Orchid is a plant that is very beautiful.");
        plant.setCategory(null);

        plants_quantity.put(plant, 1);

    }


    @AfterEach
    void tearDown() {
        order = null;
        user = null;
        plant = null;
        plants_quantity = null;
    }

    @Test
    void whenPostOrder_thenReturnOrder_andStatus200() throws Exception {

        when(orderService.createOrder(any())).thenReturn(order);

        mockMvc.perform(post("/floralfiesta/order/create")
                .contentType("application/json")
                .content("""
                        {
                            "totalPrice": 10.0,
                            "acpID": 1,
                            "userId": 1,
                            "plantQuantityMap": { "1" : 1}}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.totalPrice").value(10.0))
                .andExpect(jsonPath("$.user.userId").value(1))
                .andExpect(jsonPath("$.pickupCode").value(1234))
                .andExpect(jsonPath("$.description").value("Test"))
                .andExpect(jsonPath("$.acpID").value(1))
                .andExpect(jsonPath("$.status").value("WAITING_FOR_PICKUP"))
                .andExpect(jsonPath("$.deliveryDate").value("2021-01-01"))
                .andExpect(jsonPath("$.pickupDate").value("2021-01-01"));

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void whenPostInvalidOrder_returnStatus400() throws Exception {
        when(orderService.createOrder(any())).thenThrow(new InvalidOrderException());

        mockMvc.perform(post("/floralfiesta/order/create")
                        .contentType("application/json")
                        .content("""
                                {
                                    "totalPrice": 10.0,
                                    "userId": 1,
                                    "plantQuantityMap": { "1" : 1}}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid order."));

        verify(orderService, times(1)).createOrder(any());
    }


}
