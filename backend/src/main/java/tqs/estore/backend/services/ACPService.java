package tqs.estore.backend.services;

import org.springframework.stereotype.Service;
import tqs.estore.backend.connection.DropMateAPIClient;

import java.util.List;
import java.util.Map;

@Service
public class ACPService {

    private final DropMateAPIClient httpClient;

    public ACPService(DropMateAPIClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Map<String, String>> getAllACPs(){
        return null;
    }

}
