package tqs.estore.backend.integrationTests;


import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tqs.estore.backend.datamodel.Plant;
import tqs.estore.backend.datamodel.PlantCategory;
import tqs.estore.backend.datamodel.Status;
import tqs.estore.backend.datamodel.User;
import tqs.estore.backend.repositories.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIT {

    private final String BASE_URL = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PlantRepository plantRepository;

    private User user;

    private Plant plant;

    @Container
    private static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest")
            .withUsername("springuser")
            .withPassword("password")
            .withDatabaseName("db");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }


    @BeforeEach
    void setUp() {
        PlantCategory plantCategory = new PlantCategory();
        plantCategory.setName("Orchid");
        plantCategory.setPhoto("orchid.jpg");
        plantCategory = categoryRepository.saveAndFlush(plantCategory);

        plant = new Plant();
        plant.setName("Orchid");
        plant.setCategory(plantCategory);
        plant.setDescription("Orchid");
        plant.setPrice(10.0);
        plant.setPhoto("orchid.jpg");
        plant = plantRepository.saveAndFlush(plant);

        user = new User();
        user.setName("user");
        user.setEmail("user@email.com");
        user.setPassword("password");
        user.setPhoneNumber(123456789);
        user.setAddress("address");
        user = userRepository.saveAndFlush(user);
    }

    @AfterEach
    void tearDown() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        plantRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Order(1)
    @Test
    void whenPostOrder_thenReturnOrder_andStatus200() {
        RestAssured.given().contentType("application/json")
                .body("{\"userId\": 1, \"plantQuantityMap\": {\"1\": 1}, \"acpID\": 1, \"totalPrice\": 10.0}")
                .when()
                .post(BASE_URL + port + "/floralfiesta/order/create")
                .then()
                .statusCode(200)
                .assertThat()
                .body("orderId", org.hamcrest.Matchers.equalTo(1))
                .body("totalPrice", org.hamcrest.Matchers.equalTo(10.0F))
                .body("user.userId", org.hamcrest.Matchers.equalTo(1))
                .body("acpID", org.hamcrest.Matchers.equalTo(1));
    }


    @Order(2)
    @Test
    void whenPostOrderWithInvalidUser_thenReturnError_andStatus400() {
        RestAssured.given().contentType("application/json")
                .body("{\"userId\": 2, \"plantQuantityMap\": {\"1\": 1}, \"acpID\": 1, \"totalPrice\": 10.0}")
                .when()
                .post(BASE_URL + port + "/floralfiesta/order/create")
                .then()
                .statusCode(400)
                .assertThat()
                .body("message", org.hamcrest.Matchers.equalTo("Invalid order."));
    }



    @Order(3)
    @Test
    void whenGetDeliveredOrders_thenReturnEmpty_andStatus200() {
        RestAssured.given().contentType("application/json")
                .when()
                .get(BASE_URL + port + "/floralfiesta/order/delivered/1")
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", org.hamcrest.Matchers.equalTo(0));
    }


    @Order(4)
    @Test
    void whenGetDeliveredOrders_thenReturnOrders_andStatus200() {
        tqs.estore.backend.datamodel.Order order = new tqs.estore.backend.datamodel.Order();
        order.setUser(userRepository.findById(user.getUserId()).get());
        order.setAcpID(1);
        order.setTotalPrice(10.0);
        order.setStatus(Status.DELIVERED);
        order.setDescription("description");
        order.setPickupCode("pickupCode");
        order = orderRepository.saveAndFlush(order);


        RestAssured.given().contentType("application/json")
                .when()
                .get(BASE_URL + port + "/floralfiesta/order/delivered/" + user.getUserId())
                .then()
                .statusCode(200)
                .assertThat()
                .body("size()", org.hamcrest.Matchers.equalTo(1))
                .body("[0].orderId", org.hamcrest.Matchers.equalTo(order.getOrderId().intValue()))
                .body("[0].totalPrice", org.hamcrest.Matchers.equalTo(order.getTotalPrice().floatValue()))
                .body("[0].user.userId", org.hamcrest.Matchers.equalTo(order.getUser().getUserId().intValue()))
                .body("[0].acpID", org.hamcrest.Matchers.equalTo(order.getAcpID()));
    }
}
