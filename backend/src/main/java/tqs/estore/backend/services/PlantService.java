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

    public ResponseEntity<List<Plant>> getAllPlants(){
        return null;
    }

    public ResponseEntity<Plant> getPlantById(Long id){
        return null;
    }

    public ResponseEntity<List<Plant>> getPlantsByName(String name){
        return null;
    }

    public ResponseEntity<List<Plant>> getPlantsByCategory(String category){
        return null;
    }

}
