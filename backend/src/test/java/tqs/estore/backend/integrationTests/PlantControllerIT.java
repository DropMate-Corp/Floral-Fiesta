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
import tqs.estore.backend.repositories.CategoryRepository;
import tqs.estore.backend.repositories.PlantRepository;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlantControllerIT {

    private final String BASE_URL = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    private PlantCategory plantCategory;

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
    public void setUp(){
        plantCategory = new PlantCategory();
        plantCategory.setName("Orchid");
        plantCategory.setPhoto("orchid.jpg");

        PlantCategory plantCategory2 = new PlantCategory();
        plantCategory2.setName("Tulip");
        plantCategory2.setPhoto("tulip.jpg");

        plantCategory = categoryRepository.saveAndFlush(plantCategory);
        plantCategory2 = categoryRepository.saveAndFlush(plantCategory2);

        Plant plant1 = new Plant();
        plant1.setName("Orchid");
        plant1.setPrice(12.0);
        plant1.setPhoto("orchid.jpg");
        plant1.setDescription("Orchid is a plant that is very beautiful.");
        plant1.setCategory(plantCategory);


        Plant plant2 = new Plant();
        plant2.setName("Tulip");
        plant2.setPrice(5.0);
        plant2.setPhoto("tulip.jpg");
        plant2.setDescription("Tulip is a plant that makes people happy.");
        plant2.setCategory(plantCategory2);

        Plant plant3 = new Plant();
        plant3.setName("Spice Orchid");
        plant3.setPrice(20.0);
        plant3.setPhoto("spice_orchid.jpg");
        plant3.setDescription("Spice Orchid is from the Orchid family.");
        plant3.setCategory(plantCategory);

        plantRepository.saveAndFlush(plant1);
        plantRepository.saveAndFlush(plant2);
        plantRepository.saveAndFlush(plant3);

    }

    @AfterEach
    public void resetDB(){
        plantRepository.deleteAll();
        categoryRepository.deleteAll();
    }




    @Test
    public void whenGetAllPlants_thenReturnPlants_andStatus200(){
        RestAssured.with().contentType("application/json")
                .when().get(BASE_URL + port + "/floralfiesta/plant/all")
                .then().statusCode(200)
                .body("size()", is(3)).and()
                .body("[0].name", equalTo("Orchid")).and()
                .body("[0].price", equalTo(12.0f)).and()
                .body("[0].photo", equalTo("orchid.jpg")).and()
                .body("[0].description", equalTo("Orchid is a plant that is very beautiful.")).and()
                .body("[0].category.name", equalTo("Orchid")).and()
                .body("[0].category.photo", equalTo("orchid.jpg")).and()
                .body("[1].name", equalTo("Tulip")).and()
                .body("[1].price", equalTo(5.0f)).and()
                .body("[1].photo", equalTo("tulip.jpg")).and()
                .body("[1].description", equalTo("Tulip is a plant that makes people happy.")).and()
                .body("[1].category.name", equalTo("Tulip")).and()
                .body("[1].category.photo", equalTo("tulip.jpg")).and()
                .body("[2].name", equalTo("Spice Orchid")).and()
                .body("[2].price", equalTo(20.0f)).and()
                .body("[2].photo", equalTo("spice_orchid.jpg")).and()
                .body("[2].description", equalTo("Spice Orchid is from the Orchid family.")).and()
                .body("[2].category.name", equalTo("Orchid")).and()
                .body("[2].category.photo", equalTo("orchid.jpg"));
    }

    @Test
    public void whenGetAllPlants_thenReturnsEmptyList_andStatus200(){
        plantRepository.deleteAll();
        RestAssured.with().contentType("application/json")
                .when().get(BASE_URL + port + "/floralfiesta/plant/all")
                .then().statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void whenGetPlantsByName_thenReturnPlants_andStatus200() {
        RestAssured.with().contentType("application/json")
                .when().get(BASE_URL + port + "/floralfiesta/plant/name/Orchid")
                .then().statusCode(200)
                .body("size()", is(2)).and()
                .body("[0].name", equalTo("Orchid")).and()
                .body("[0].price", equalTo(12.0f)).and()
                .body("[0].photo", equalTo("orchid.jpg")).and()
                .body("[0].description", equalTo("Orchid is a plant that is very beautiful.")).and()
                .body("[0].category.name", equalTo("Orchid")).and()
                .body("[0].category.photo", equalTo("orchid.jpg")).and()
                .body("[1].name", equalTo("Spice Orchid")).and()
                .body("[1].price", equalTo(20.0f)).and()
                .body("[1].photo", equalTo("spice_orchid.jpg")).and()
                .body("[1].description", equalTo("Spice Orchid is from the Orchid family.")).and()
                .body("[1].category.name", equalTo("Orchid")).and()
                .body("[1].category.photo", equalTo("orchid.jpg"));
    }

    @Test
    public void whenGetPlantsByName_thenReturnEmptyList_andStatus200() {
        RestAssured.with().contentType("application/json")
                .when().get(BASE_URL + port + "/floralfiesta/plant/name/Orchiddd")
                .then().statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void whenGetPlantsByCategory_thenReturnPlants_andStatus200() {
        RestAssured.with().contentType("application/json")
                .when().get(BASE_URL + port + "/floralfiesta/plant/category/" + plantCategory.getCategoryId())
                .then().statusCode(200)
                .body("size()", is(2)).and()
                .body("[0].name", equalTo("Orchid")).and()
                .body("[0].price", equalTo(12.0f)).and()
                .body("[0].photo", equalTo("orchid.jpg")).and()
                .body("[0].description", equalTo("Orchid is a plant that is very beautiful.")).and()
                .body("[0].category.name", equalTo("Orchid")).and()
                .body("[0].category.photo", equalTo("orchid.jpg")).and()
                .body("[1].name", equalTo("Spice Orchid")).and()
                .body("[1].price", equalTo(20.0f)).and()
                .body("[1].photo", equalTo("spice_orchid.jpg")).and()
                .body("[1].description", equalTo("Spice Orchid is from the Orchid family.")).and()
                .body("[1].category.name", equalTo("Orchid")).and()
                .body("[1].category.photo", equalTo("orchid.jpg"));
    }

    @Test
    public void whenGetPlantsByCategory_thenReturnEmptyList_andStatus200() {
        RestAssured.with().contentType("application/json")
                .when().get(BASE_URL + port + "/floralfiesta/plant/category/999")
                .then().statusCode(200)
                .body("size()", is(0));
    }

}
