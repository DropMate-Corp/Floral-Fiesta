package tqs.estore.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.estore.backend.datamodel.Plant;
import tqs.estore.backend.services.PlantService;

import java.util.List;


@RestController
@RequestMapping("floralfiesta/plant")
@CrossOrigin
public class PlantController {
    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Plant>> getAllPlants(){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plant> getPlantById(Long id){
        return null;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Plant>> getPlantsByName(String name){
        return null;
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Plant>> getPlantsByCategory(String category){
        return null;
    }




}
