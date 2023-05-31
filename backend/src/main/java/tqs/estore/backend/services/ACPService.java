package tqs.estore.backend.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import tqs.estore.backend.connection.DropMateAPIClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ACPService {

    private final DropMateAPIClient httpClient;

    public ACPService(DropMateAPIClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Map<String, String>> getAllACPs() throws URISyntaxException, ParseException, IOException {
        List<Map<String, String>> acps = new ArrayList<>();
        JSONArray acps_json = httpClient.getAvaiableACP();
        for (Object acp : acps_json) {
            Map<String, String> acp_map = new HashMap<>();
            acp_map.put("acpId", ((JSONObject) acp).get("acpId").toString());
            acp_map.put("name", ((JSONObject) acp).get("name").toString());
            acp_map.put("city", ((JSONObject) acp).get("city").toString());
            acp_map.put("address", ((JSONObject) acp).get("address").toString());
            acps.add(acp_map);
        }
        return acps;

    }

}
