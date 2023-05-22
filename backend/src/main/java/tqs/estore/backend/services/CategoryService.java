package tqs.estore.backend.services;
import org.springframework.stereotype.Service;
import tqs.estore.backend.datamodel.PlantCategory;
import tqs.estore.backend.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    public List<PlantCategory> getAllCategories(){
        return null;
    }
}
