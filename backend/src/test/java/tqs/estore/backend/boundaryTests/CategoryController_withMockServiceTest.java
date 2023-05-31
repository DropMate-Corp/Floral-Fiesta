package tqs.estore.backend.boundaryTests;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.estore.backend.controllers.CategoryController;
import tqs.estore.backend.services.CategoryService;
import tqs.estore.backend.datamodel.PlantCategory;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
class CategoryController_withMockServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private List<PlantCategory> categories;

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

        categories = List.of(plantCategory, plantCategory2);
    }

    @AfterEach
    public void tearDown() {
        categories = null;
    }

    @Test
    void whenGetAllCategories_thenReturnCategories_andStatus200() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(categories);
        mockMvc.perform(get("/floralfiesta/category/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].categoryId").value(categories.get(0).getCategoryId()))
                .andExpect(jsonPath("$[0].name").value(categories.get(0).getName()))
                .andExpect(jsonPath("$[0].photo").value(categories.get(0).getPhoto()))
                .andExpect(jsonPath("$[1].categoryId").value(categories.get(1).getCategoryId()))
                .andExpect(jsonPath("$[1].name").value(categories.get(1).getName()))
                .andExpect(jsonPath("$[1].photo").value(categories.get(1).getPhoto()));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void whenGetAllCategories_thenReturnEmptyList_andStatus200() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of());
        mockMvc.perform(get("/floralfiesta/category/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(categoryService, times(1)).getAllCategories();
    }

}
