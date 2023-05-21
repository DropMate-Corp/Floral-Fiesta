package tqs.estore.backend.services;

import org.springframework.stereotype.Service;
import tqs.estore.backend.datamodel.Plant;
import tqs.estore.backend.repositories.PlantRepository;

import java.util.List;

@Service
public class PlantService {
    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public List<Plant> getAllPlants(){
        return plantRepository.findAll();
    }

    public Plant getPlantById(Long id){
        return null;
    }

    public List<Plant> getPlantsByName(String name){
        return plantRepository.findByNameContaining(name);
    }

    public List<Plant> getPlantsByCategory(Integer categoryID){
        return plantRepository.findByCategoryCategoryId(categoryID.longValue());
    }

}
