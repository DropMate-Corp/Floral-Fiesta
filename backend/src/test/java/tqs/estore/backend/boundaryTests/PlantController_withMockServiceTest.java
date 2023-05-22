package tqs.estore.backend.boundaryTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.estore.backend.controllers.PlantController;
import tqs.estore.backend.datamodel.PlantCategory;
import tqs.estore.backend.services.PlantService;

import java.util.ArrayList;
import java.util.List;
import tqs.estore.backend.datamodel.Plant;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlantController.class)
class PlantController_withMockServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlantService plantService;

    private List<Plant> plants;
    private List<Plant> plantsByName;

    @BeforeEach
    public void setUp() {
        PlantCategory plantCategory = new PlantCategory();
        plantCategory.setCategoryId(1L);
        plantCategory.setName("Orchid");
        plantCategory.setPhoto("orchid.jpg");

        PlantCategory plantCategory2 = new PlantCategory();
        plantCategory2.setCategoryId(2L);
        plantCategory2.setName("Tulip");
        plantCategory2.setPhoto("tulip.jpg");

        plants = new ArrayList<>();
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


        plants.add(plant1);
        plants.add(plant2);
        plants.add(plant3);

        plantsByName = new ArrayList<>();
        plantsByName.add(plant1);
        plantsByName.add(plant3);

    }

    @AfterEach
    public void tearDown() {
        plants = null;
        plantsByName = null;
    }

    @Test
    void whenGetAllPlants_thenReturnPlants_andStatus200() throws Exception {
        when(plantService.getAllPlants()).thenReturn(plants);

        mockMvc.perform(get("/floralfiesta/plant/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value(plants.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(plants.get(0).getPrice()))
                .andExpect(jsonPath("$[0].photo").value(plants.get(0).getPhoto()))
                .andExpect(jsonPath("$[0].description").value(plants.get(0).getDescription()))
                .andExpect(jsonPath("$[0].category.name").value(plants.get(0).getCategory().getName()))
                .andExpect(jsonPath("$[1].name").value(plants.get(1).getName()))
                .andExpect(jsonPath("$[1].price").value(plants.get(1).getPrice()))
                .andExpect(jsonPath("$[1].photo").value(plants.get(1).getPhoto()))
                .andExpect(jsonPath("$[1].description").value(plants.get(1).getDescription()))
                .andExpect(jsonPath("$[1].category.name").value(plants.get(1).getCategory().getName()))
                .andExpect(jsonPath("$[2].name").value(plants.get(2).getName()))
                .andExpect(jsonPath("$[2].price").value(plants.get(2).getPrice()))
                .andExpect(jsonPath("$[2].photo").value(plants.get(2).getPhoto()))
                .andExpect(jsonPath("$[2].description").value(plants.get(2).getDescription()))
                .andExpect(jsonPath("$[2].category.name").value(plants.get(2).getCategory().getName()));


        verify(plantService, times(1)).getAllPlants();
    }

    @Test
    void whenGetAllPlants_thenReturnEmptyList_andStatus200() throws Exception {
        when(plantService.getAllPlants()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/floralfiesta/plant/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(plantService, times(1)).getAllPlants();
    }

    @Test
    void whenGetPlantsByName_thenReturnPlants_andStatus200() throws Exception {
        when(plantService.getPlantsByName("Orchid")).thenReturn(plantsByName);

        mockMvc.perform(get("/floralfiesta/plant/name/Orchid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(plantsByName.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(plantsByName.get(0).getPrice()))
                .andExpect(jsonPath("$[0].photo").value(plantsByName.get(0).getPhoto()))
                .andExpect(jsonPath("$[0].description").value(plantsByName.get(0).getDescription()))
                .andExpect(jsonPath("$[1].name").value(plantsByName.get(1).getName()))
                .andExpect(jsonPath("$[1].price").value(plantsByName.get(1).getPrice()))
                .andExpect(jsonPath("$[1].photo").value(plantsByName.get(1).getPhoto()))
                .andExpect(jsonPath("$[1].description").value(plantsByName.get(1).getDescription()));

        verify(plantService, times(1)).getPlantsByName("Orchid");
    }

    @Test
    void whenGetPlantsByName_thenReturnEmptyList_andStatus200() throws Exception {
        when(plantService.getPlantsByName("Violet")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/floralfiesta/plant/name/Violet"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(plantService, times(1)).getPlantsByName("Violet");
    }

    @Test
    void whenGetPlantsByCategory_thenReturnPlants_andStatus200() throws Exception {
        when(plantService.getPlantsByCategory(1)).thenReturn(plantsByName);

        mockMvc.perform(get("/floralfiesta/plant/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(plantsByName.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(plantsByName.get(0).getPrice()))
                .andExpect(jsonPath("$[0].photo").value(plantsByName.get(0).getPhoto()))
                .andExpect(jsonPath("$[0].description").value(plantsByName.get(0).getDescription()))
                .andExpect(jsonPath("$[1].name").value(plantsByName.get(1).getName()))
                .andExpect(jsonPath("$[1].price").value(plantsByName.get(1).getPrice()))
                .andExpect(jsonPath("$[1].photo").value(plantsByName.get(1).getPhoto()))
                .andExpect(jsonPath("$[1].description").value(plantsByName.get(1).getDescription()));

        verify(plantService, times(1)).getPlantsByCategory(1);
    }

    @Test
    void whenGetPlantsByCategory_thenReturnEmptyList_andStatus200() throws Exception {
        when(plantService.getPlantsByCategory(3)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/floralfiesta/plant/category/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(plantService, times(1)).getPlantsByCategory(3);
    }

    @Test
    void whenGetPlantById_thenReturnPlant_andStatus200() throws Exception {
        Plant plant1 = plants.get(0);

        when(plantService.getPlantById(1L)).thenReturn(plant1);

        mockMvc.perform(get("/floralfiesta/plant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(plant1.getName()))
                .andExpect(jsonPath("$.price").value(plant1.getPrice()))
                .andExpect(jsonPath("$.photo").value(plant1.getPhoto()))
                .andExpect(jsonPath("$.description").value(plant1.getDescription()))
                .andExpect(jsonPath("$.category.name").value(plant1.getCategory().getName()));

        verify(plantService, times(1)).getPlantById(1L);
    }
}
