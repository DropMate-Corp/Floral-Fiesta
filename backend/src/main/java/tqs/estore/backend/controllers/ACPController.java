package tqs.estore.backend.controllers;

import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.estore.backend.services.ACPService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("floralfiesta/acp")
@CrossOrigin(origins = "https://dropmate-corp.github.io/Floral-Fiesta-UI/", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})

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
