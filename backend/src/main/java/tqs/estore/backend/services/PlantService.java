package tqs.estore.backend.services;

import org.springframework.stereotype.Service;
import tqs.estore.backend.datamodel.Plant;
import tqs.estore.backend.exceptions.PlantNotFoundException;
import tqs.estore.backend.repositories.PlantRepository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Get plant from floralfiesta database by id
     * @param id - id of the plant
     * @return Plant
     * @throws PlantNotFoundException - if plant with id is not found
     */
    public Plant getPlantById(Long id) throws PlantNotFoundException {
        Optional<Plant> plant = plantRepository.findById(id);
        if(plant.isPresent()){
            return plant.get();
        }
        throw new PlantNotFoundException();
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
