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
import tqs.estore.backend.datamodel.PlantCategory;
import tqs.estore.backend.repositories.CategoryRepository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryControllerIT {

    private final String BASE_URL = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private CategoryRepository categoryRepository;

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
    public void setUp() {
        PlantCategory plantCategory = new PlantCategory();
        plantCategory.setName("Orchid");
        plantCategory.setPhoto("orchid.jpg");

        PlantCategory plantCategory2 = new PlantCategory();
        plantCategory2.setName("Tulip");
        plantCategory2.setPhoto("tulip.jpg");

        categoryRepository.saveAndFlush(plantCategory);
        categoryRepository.saveAndFlush(plantCategory2);
    }

    @AfterEach
    public void resetDB(){
        categoryRepository.deleteAll();
    }

    @Test
    void whenGetAllCategories_thenReturnCategories_andStatus200() {
        RestAssured.get(BASE_URL + port + "/floralfiesta/category/all")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].name", equalTo("Orchid"))
                .body("[0].photo", equalTo("orchid.jpg"))
                .body("[1].name", equalTo("Tulip"))
                .body("[1].photo", equalTo("tulip.jpg"));
    }

    @Test
    void whenGetAllCategories_thenReturnEmptyList_andStatus200() {
        categoryRepository.deleteAll();

        RestAssured.get(BASE_URL + port + "/floralfiesta/category/all")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

}
