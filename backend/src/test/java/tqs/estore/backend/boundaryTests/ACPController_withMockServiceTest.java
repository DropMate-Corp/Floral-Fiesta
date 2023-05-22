package tqs.estore.backend.boundaryTests;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tqs.estore.backend.controllers.ACPController;
import org.springframework.test.web.servlet.MockMvc;
import tqs.estore.backend.services.ACPService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ACPController.class)
public class ACPController_withMockServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ACPService acpService;

    private List<Map<String, String>> acps;

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

        acps = new ArrayList<>();

        acps.add(acp1);
        acps.add(acp2);
    }

    @AfterEach
    void tearDown() {
        acps = null;
    }

    @Test
    void whenGetAllACPs_thenReturnACPs_andStatus200() throws Exception {
        when(acpService.getAllACPs()).thenReturn(acps);
        mockMvc.perform(get("/floralfiesta/acp/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].acpId").value(acps.get(0).get("acpId")))
                .andExpect(jsonPath("$[0].name").value(acps.get(0).get("name")))
                .andExpect(jsonPath("$[0].city").value(acps.get(0).get("city")))
                .andExpect(jsonPath("$[0].address").value(acps.get(0).get("address")))
                .andExpect(jsonPath("$[1].acpId").value(acps.get(1).get("acpId")))
                .andExpect(jsonPath("$[1].name").value(acps.get(1).get("name")))
                .andExpect(jsonPath("$[1].city").value(acps.get(1).get("city")))
                .andExpect(jsonPath("$[1].address").value(acps.get(1).get("address")));
    }

    @Test
    void whenGetAllACPs_thenReturnEmptyList_andStatus200() throws Exception {
        when(acpService.getAllACPs()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/floralfiesta/acp/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

}
