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

    /**
     * Get all plants from floralfiesta database
     * @return List of plants
     */
    public List<Plant> getAllPlants(){
        return plantRepository.findAll();
    }

    public Plant getPlantById(Long id){
        return null;
    }

    /**
     * Get plants from floralfiesta database by name
     * @param name - name of the plant
     * @return List of plants
     */

    public List<Plant> getPlantsByName(String name){
        return plantRepository.findByNameContaining(name);
    }

    /**
     * Get plants from floralfiesta database by category
     * @param categoryID - id of the category
     * @return List of plants
     */
    public List<Plant> getPlantsByCategory(Integer categoryID){
        return plantRepository.findByCategoryCategoryId(categoryID.longValue());
    }

}
