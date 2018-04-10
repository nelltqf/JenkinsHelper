package happy.rabbit.http;

import happy.rabbit.utils.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class Request {

    private static final Logger LOGGER = LoggerFactory.getLogger(Request.class);

    // TODO should this client be static? Threadsafe?
    private HttpClient client = HttpClientBuilder.create().build();
    private HttpRequestBase requestBase;

    private Map<String, String> headers = new TreeMap<>();
    private List<NameValuePair> formFields = new ArrayList<>();

    private Request() {

    }

    public static Request get(String url) {
        Request request = new Request();
        request.requestBase = new HttpGet(url);
        return request;
    }

    public static Request post(String url) {
        Request request = new Request();
        request.requestBase = new HttpPost(url);
        return request;
    }

    public Request withBasicAuth(String username, String password) {
        String usernameAndPassword = username + ":" + password;
        usernameAndPassword = new String(Base64.getEncoder().encode(usernameAndPassword.getBytes()));
        headers.put("Authorization", "Basic " + usernameAndPassword);
        return this;
    }

    public Request withHeader(String header, String value) {
        headers.put(header, value);
        return this;
    }

    public Request withFormField(String name, String value) {
        formFields.add(new BasicNameValuePair(name, value));
        return this;
    }

    public HttpResponse asResponse() {
        return executeAndReturnResponse();
    }

    public String asString() {
        HttpResponse response = executeAndReturnResponse();
        return Utils.httpResponseAsString(response);
    }

    public JSONObject asJson() {
        String asString = asString();
        return new JSONObject(asString);
    }

    private HttpResponse executeAndReturnResponse() {
        try {
            headers.forEach((header, value) -> requestBase.addHeader(header, value));
            if (!formFields.isEmpty() && requestBase instanceof HttpPost) {
                ((HttpPost) requestBase).setEntity(new UrlEncodedFormEntity(formFields));
            }
            return client.execute(requestBase);
        } catch (IOException e) {
            LOGGER.error("Error while executing request", e);
            throw new IllegalStateException(e);
        }
    }
}
