package tqs.estore.backend.serviceTests;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.estore.backend.connection.DropMateAPIClient;
import tqs.estore.backend.services.ACPService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ACPService_UnitTest {
    @Mock
    private DropMateAPIClient httpClient;

    @InjectMocks
    private ACPService acpService;

    private JSONArray acps;

    @BeforeEach
    void setUp() {
        Map<String, String> acp1 = new HashMap<>();
        acp1.put("acpId", "1");
        acp1.put("name", "ACP1");
        acp1.put("city", "Aveiro");
        acp1.put("address", "Rua 1");

        Map<String, String> acp2 = new HashMap<>();
        acp2.put("acpId", "2");
        acp2.put("name", "ACP2");
        acp2.put("city", "Porto");
        acp2.put("address", "Rua 2");

        List<Map<String, String>> acpsList = new ArrayList<>();

        acpsList.add(acp1);
        acpsList.add(acp2);


        acps = new JSONArray();

        for (Map<String, String> acp : acpsList) {
            JSONObject acpJson = new JSONObject(acp);
            acps.add(acpJson);
        }

    }

    @AfterEach
    void tearDown() {
        acps = null;
    }

    @Test
    void whenGetAllACPs_thenReturnACPs() throws URISyntaxException, ParseException, IOException {
        when(httpClient.getAvaiableACP()).thenReturn(acps);

        List<Map<String, String>> acpsList = acpService.getAllACPs();

        assertThat(acpsList).hasSize(2);
        assertThat(acpsList.get(0).get("acpId")).isEqualTo("1");
        assertThat(acpsList.get(0).get("name")).isEqualTo("ACP1");
        assertThat(acpsList.get(0).get("city")).isEqualTo("Aveiro");
        assertThat(acpsList.get(0).get("address")).isEqualTo("Rua 1");
        assertThat(acpsList.get(1).get("acpId")).isEqualTo("2");
        assertThat(acpsList.get(1).get("name")).isEqualTo("ACP2");
        assertThat(acpsList.get(1).get("city")).isEqualTo("Porto");
        assertThat(acpsList.get(1).get("address")).isEqualTo("Rua 2");

        verify(httpClient, times(1)).getAvaiableACP();
    }

    @Test
    void whenGetAllACPs_thenReturnEmptyList() throws URISyntaxException, ParseException, IOException {
        when(httpClient.getAvaiableACP()).thenReturn(null);

        List<Map<String, String>> acpsList = acpService.getAllACPs();

        assertThat(acpsList).hasSize(0);

        verify(httpClient, times(1)).getAvaiableACP();
    }

}
