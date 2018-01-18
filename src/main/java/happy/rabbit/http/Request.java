package happy.rabbit.http;

import com.mashape.unirest.http.utils.Base64Coder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Request {

    private HttpClient client = HttpClientBuilder.create().build();
    private HttpGet request;
    private Map<String, String> headers = new TreeMap<>();

    private String endpoint;

    public Request withBasicAuth(String username, String password) {
        headers.put("Authorization", "Basic " + Base64Coder.encodeString(username + ":" + password));
        return this;
    }

    public Request withHeader(String header, String value) {
        headers.put(header, value);
        return this;
    }

    public String execute() {
        headers.forEach((header, value) -> request.addHeader(header, value));
        try {
            client.execute(request);
        } catch (IOException e) {
            // TODO do something
        }
    }

}
