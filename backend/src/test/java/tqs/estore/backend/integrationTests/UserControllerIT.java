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
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class UserControllerIT {
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
    void whenRegisterValidUser_thenReturnUser_andStatus200() {
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
    void whenRegisterInvalidUser_thenReturnStatus409() {
        RestAssured.with().contentType("application/json")
                .when().post(BASE_URL + port + "/floralfiesta/user/register?name=User&email=user@email.com&password=password&phoneNumber=123456789&address=Address")
                .then().statusCode(409)
                .assertThat().body("message", equalTo("Email address is already registered."));
    }

    @Test
    @Order(3)
    void whenLoginValidUser_thenReturnUser_andStatus200() {
        RestAssured.with().contentType("application/json")
                .when().post(BASE_URL + port + "/floralfiesta/user/login?email=" + "user@email.com" + "&password=" + "password")
                .then().statusCode(200)
                .assertThat().body("name", equalTo("User"))
                .assertThat().body("email", equalTo("user@email.com"))
                .assertThat().body("password", equalTo("password"))
                .assertThat().body("phoneNumber", equalTo(123456789))
                .assertThat().body("address", equalTo("Address"));
    }

    @Test
    @Order(4)
    void whenLoginWithInvalidEmail_thenReturnStatus401() {
        RestAssured.with().contentType("application/json")
                .when().post(BASE_URL + port + "/floralfiesta/user/login?email=" + "invalidemail@email.com" + "&password=" + "password")
                .then().statusCode(401)
                .assertThat().body("message", equalTo("Invalid login credentials."));
    }

    @Test
    @Order(5)
    void whenLoginWithInvalidPassword_thenReturnStatus401() {
        RestAssured.with().contentType("application/json")
                .when().post(BASE_URL + port + "/floralfiesta/user/login?email=" + "user@email.com" + "&password=" + "invalidPassword")
                .then().statusCode(401)
                .assertThat().body("message", equalTo("Invalid login credentials."));
    }

}
