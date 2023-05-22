package tqs.estore.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.estore.backend.services.ACPService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("floralfiesta/acp")
@CrossOrigin
public class ACPController {
    private final ACPService acpService;

    public ACPController(ACPService acpService) {
        this.acpService = acpService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllACPs(){
        return new ResponseEntity<>(acpService.getAllACPs(), org.springframework.http.HttpStatus.OK);
    }

}
