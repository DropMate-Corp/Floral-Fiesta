package tqs.estore.backend.services;

import org.springframework.http.ResponseEntity;
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
        return null;
    }

    public Plant getPlantById(Long id){
        return null;
    }

    public List<Plant> getPlantsByName(String name){
        return null;
    }

    public List<Plant> getPlantsByCategory(Integer categoryID){
        return null;
    }

}
