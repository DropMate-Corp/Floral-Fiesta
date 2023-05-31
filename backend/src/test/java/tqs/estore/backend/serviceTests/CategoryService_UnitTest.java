package tqs.estore.backend.serviceTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.estore.backend.datamodel.PlantCategory;
import tqs.estore.backend.repositories.CategoryRepository;
import tqs.estore.backend.services.CategoryService;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CategoryService_UnitTest {
    @Mock
    private CategoryRepository categoryRepository;

   @InjectMocks
   private CategoryService categoryService;

    private List<PlantCategory> categories;

    @BeforeEach
    void setUp() {
        PlantCategory plantCategory = new PlantCategory();
        plantCategory.setCategoryId(1L);
        plantCategory.setName("Orchid");
        plantCategory.setPhoto("orchid.jpg");

        PlantCategory plantCategory2 = new PlantCategory();
        plantCategory2.setCategoryId(2L);
        plantCategory2.setName("Tulip");
        plantCategory2.setPhoto("tulip.jpg");

        categories = List.of(plantCategory, plantCategory2);
    }

    @AfterEach
    void tearDown() {
        categories = null;
    }

    @Test
    void whenGetAllCategories_thenReturnCategories() {
        when(categoryRepository.findAll()).thenReturn(categories);
        List<PlantCategory> foundCategories = categoryService.getAllCategories();
        assertThat(foundCategories).isEqualTo(categories);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void whenGetAllCategories_thenReturnEmptyList() {
        when(categoryRepository.findAll()).thenReturn(List.of());
        List<PlantCategory> foundCategories = categoryService.getAllCategories();
        assertThat(foundCategories).isEqualTo(List.of());
        verify(categoryRepository, times(1)).findAll();
    }

}
