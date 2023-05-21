package tqs.estore.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return new ResponseEntity<>(plantService.getAllPlants(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id){
        return null;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Plant>> getPlantsByName(@PathVariable String name){
        return new ResponseEntity<>(plantService.getPlantsByName(name), HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Plant>> getPlantsByCategory(@PathVariable String category){
        return new ResponseEntity<>(plantService.getPlantsByCategory(category), HttpStatus.OK);
    }




}
