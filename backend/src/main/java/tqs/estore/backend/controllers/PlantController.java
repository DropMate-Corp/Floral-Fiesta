package tqs.estore.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.estore.backend.datamodel.Plant;
import tqs.estore.backend.exceptions.PlantNotFoundException;
import tqs.estore.backend.services.PlantService;

import java.util.List;


@RestController
@RequestMapping("floralfiesta/plant")
@CrossOrigin(origins = "https://dropmate-corp.github.io/Floral-Fiesta-UI/")
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
    public ResponseEntity<Plant> getPlantById(@PathVariable Long id) throws PlantNotFoundException {
        return new ResponseEntity<>(plantService.getPlantById(id), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Plant>> getPlantsByName(@PathVariable String name){
        return new ResponseEntity<>(plantService.getPlantsByName(name), HttpStatus.OK);
    }

    @GetMapping("/category/{categoryID}")
    public ResponseEntity<List<Plant>> getPlantsByCategory(@PathVariable Integer categoryID){
        return new ResponseEntity<>(plantService.getPlantsByCategory(categoryID), HttpStatus.OK);
    }




}
