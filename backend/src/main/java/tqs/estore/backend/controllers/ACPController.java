package tqs.estore.backend.controllers;

import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.estore.backend.services.ACPService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("floralfiesta/acp")
@CrossOrigin(origins = "https://dropmate-corp.github.io/Floral-Fiesta-UI/")
public class ACPController {
    private final ACPService acpService;

    public ACPController(ACPService acpService) {
        this.acpService = acpService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, String>>> getAllACPs() throws URISyntaxException, ParseException, IOException {
        return new ResponseEntity<>(acpService.getAllACPs(), org.springframework.http.HttpStatus.OK);
    }

}
