package tqs.estore.backend.connectionTests;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tqs.estore.backend.connection.DropMateAPIClient;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class DropMateAPIClient_UnitTest {


    private DropMateAPIClient dropMateAPIClient;

    @BeforeEach
    public void setUp() {
        dropMateAPIClient = new DropMateAPIClient();
    }

    @Test
    public void testPostOrder() throws URISyntaxException, ParseException, IOException {
        JSONObject response = dropMateAPIClient.postOrder(1, 1);
        assertThat(response.get("delivery_date")).isNotNull();
        assertThat(response.get("pickup_code")).isNotNull();
        assertThat(response.get("status")).isNotNull();
    }

    @Test
    public void testGetAvaiableACP() throws URISyntaxException, ParseException, IOException {
        JSONArray response = dropMateAPIClient.getAvaiableACP();
        assertThat(response.size()).isNotZero();
    }



}
