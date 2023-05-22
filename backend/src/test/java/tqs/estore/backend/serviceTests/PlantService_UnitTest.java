package tqs.estore.backend.serviceTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.estore.backend.datamodel.Plant;
import tqs.estore.backend.datamodel.PlantCategory;
import tqs.estore.backend.repositories.PlantRepository;
import tqs.estore.backend.services.PlantService;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlantService_UnitTest {
    @Mock
    private PlantRepository plantRepository;

    @InjectMocks
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
    public void whenGetAllPlants_thenReturnPlants() {
        when(plantRepository.findAll()).thenReturn(plants);

        List<Plant> foundPlants = plantService.getAllPlants();

        assertThat(foundPlants).isEqualTo(plants);
        verify(plantRepository, times(1)).findAll();
    }

    @Test
    public void whenGetAllPlants_thenReturnsEmptyList() {
        when(plantRepository.findAll()).thenReturn(new ArrayList<>());

        List<Plant> foundPlants = plantService.getAllPlants();

        assertThat(foundPlants).isEqualTo(new ArrayList<>());
        verify(plantRepository, times(1)).findAll();
    }

    @Test
    public void whenGetPlantsByName_thenReturnPlants() {
        when(plantRepository.findByNameContaining("Orchid")).thenReturn(plantsByName);

        List<Plant> foundPlants = plantService.getPlantsByName("Orchid");

        assertThat(foundPlants).isEqualTo(plantsByName);
        verify(plantRepository, times(1)).findByNameContaining("Orchid");
    }

    @Test
    public void whenGetPlantsByName_thenReturnsEmptyList() {
        when(plantRepository.findByNameContaining("Tulip")).thenReturn(new ArrayList<>());

        List<Plant> foundPlants = plantService.getPlantsByName("Tulip");

        assertThat(foundPlants).isEqualTo(new ArrayList<>());
        verify(plantRepository, times(1)).findByNameContaining("Tulip");
    }

    @Test
    public void whenGetPlantsByCategory_thenReturnPlants() {
        when(plantRepository.findByCategoryCategoryId(1L)).thenReturn(plantsByName);

        List<Plant> foundPlants = plantService.getPlantsByCategory(1);

        assertThat(foundPlants).isEqualTo(plantsByName);
        verify(plantRepository, times(1)).findByCategoryCategoryId(1L);
    }

    @Test
    public void whenGetPlantsByCategory_thenReturnsEmptyList() {
        when(plantRepository.findByCategoryCategoryId(3L)).thenReturn(new ArrayList<>());

        List<Plant> foundPlants = plantService.getPlantsByCategory(3);

        assertThat(foundPlants).isEqualTo(new ArrayList<>());
        verify(plantRepository, times(1)).findByCategoryCategoryId(3L);
    }



}
