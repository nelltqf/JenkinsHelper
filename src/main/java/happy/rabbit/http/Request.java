package happy.rabbit.http;

import org.apache.commons.io.IOUtils;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Request {

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
        try {
            InputStream inputStream = response.getEntity().getContent();
            return new String(IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
            // TODO logging
            throw new IllegalStateException(e);
        }
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
            // TODO logging
            throw new IllegalStateException(e);
        }
    }
}
