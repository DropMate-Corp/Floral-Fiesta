package tqs.estore.backend.boundaryTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.estore.backend.controllers.OrderController;
import tqs.estore.backend.datamodel.Order;
import tqs.estore.backend.datamodel.Plant;
import tqs.estore.backend.datamodel.Status;
import tqs.estore.backend.datamodel.User;
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
        order.setDeliveryDate(new Date(2021, 1, 1));
        order.setPickupDate(new Date(2021, 1, 1));

        plants_quantity = new HashMap<>();
        plant = new Plant();
        plant.setPlantId(1L);
        plant.setName("Orchid");
        plant.setPrice(12.0);
        plant.setPhoto("orchid.jpg");
        plant.setDescription("Orchid is a plant that is very beautiful.");
        plant.setCategory(null);

        plants_quantity.put(plant, 1);
        order.setPlantQuanityMap(plants_quantity);

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
        when(orderService.createOrder(order)).thenReturn(order);

        mockMvc.perform(post("/floralfiesta/order/create")
                .contentType("application/json")
                .content("{\n" +
                        "    \"totalPrice\": 10.0,\n" +
                        "    \"acpID\": 1,\n" +
                        "     \"userId\": 1,\n" +
                        "     \"plantQuantityMap\": { \"1\" : 1}" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.totalPrice").value(10.0))
                .andExpect(jsonPath("$.user.userId").value(1))
                .andExpect(jsonPath("$.pickupCode").value(1234))
                .andExpect(jsonPath("$.description").value("Test"))
                .andExpect(jsonPath("$.acpID").value(1))
                .andExpect(jsonPath("$.status").value("WAITING_FOR_PICKUP"))
                .andExpect(jsonPath("$.deliveryDate").value("2021-01-01"))
                .andExpect(jsonPath("$.pickupDate").value("2021-01-01"))
                .andExpect(jsonPath("$.plantQuanityMap[1]").value(1));
    }


}
