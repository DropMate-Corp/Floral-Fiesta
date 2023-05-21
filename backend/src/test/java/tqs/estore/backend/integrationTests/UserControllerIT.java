package tqs.estore.backend.integrationTests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UserControllerIT {
    private final String BASE_URL = "http://localhost:";

    @LocalServerPort
    private int port;

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

    @Test
    @Order(1)
    public void whenRegisterValidUser_thenReturnUser_andStatus200() {
        RestAssured.with().contentType("application/json")
                .when().post(BASE_URL + port + "/floralfiesta/user/register?name=User&email=user@email.com&password=password&phoneNumber=123456789&address=Address")
                .then().statusCode(200)
                .assertThat().body("name", equalTo("User"))
                .assertThat().body("email", equalTo("user@email.com"))
                .assertThat().body("password", equalTo("password"))
                .assertThat().body("phoneNumber", equalTo(123456789))
                .assertThat().body("address", equalTo("Address"));
    }

    @Test
    @Order(2)
    public void whenRegisterInvalidUser_thenReturnStatus409() {
        RestAssured.with().contentType("application/json")
                .when().post(BASE_URL + port + "/floralfiesta/user/register?name=User&email=user@email.com&password=password&phoneNumber=123456789&address=Address")
                .then().statusCode(409)
                .assertThat().body("message", equalTo("Email address is already registered."));
    }

}
