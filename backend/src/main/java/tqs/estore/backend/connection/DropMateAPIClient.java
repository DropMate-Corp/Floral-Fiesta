package tqs.estore.backend.connection;

import org.apache.http.HttpEntity;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class DropMateAPIClient  {
    private final String BASE_URL = "http://localhost:8080/dropmate/estore_api";

    public String doHttpPost(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try (CloseableHttpResponse response = client.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        }
    }

    public String doHttpGet(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = client.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        }

    }


    public JSONObject postOrder(Integer acpID, Integer storeID) throws URISyntaxException, ParseException, IOException {
        URIBuilder builder = new URIBuilder(BASE_URL + "/parcel");
        builder.addParameter("acpID", acpID.toString());
        builder.addParameter("storeID", storeID.toString());
        String url = builder.build().toString();
        String response = doHttpPost(url);
        return (JSONObject) new JSONParser().parse(response);
    }

    public JSONArray getAvaiableACP() throws URISyntaxException, ParseException, IOException {
        URIBuilder builder = new URIBuilder(BASE_URL + "/acp");
        String url = builder.build().toString();
        String response = doHttpGet(url);
        return (JSONArray) new JSONParser().parse(response);
    }






}
