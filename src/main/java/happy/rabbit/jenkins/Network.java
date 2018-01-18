package happy.rabbit.jenkins;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Network {

    //TODO move to properties
    private final static String BASE_URL = "http://localhost:49001/";
    private final static String JOB = "job/TestJob/";

    private final static String RSS_ALL = BASE_URL + JOB + "rssAll";
    private final static String UPDATE_DESCRIPTION = BASE_URL + JOB + "{id}/configSubmit";
    private final static String CRUMB = BASE_URL + "/crumbIssuer/api/json";

    // TODO hide
    private static final String username = "admin";
    private static final String password = "admin";

    private static String jenkinsCrumb;

    public Network() {
        if (jenkinsCrumb == null) {
            HttpResponse<JsonNode> crumbResponse = null;
            try {
                crumbResponse = Unirest.get(CRUMB)
                        .basicAuth(username, password)
                        .asJson();
                jenkinsCrumb = crumbResponse.getBody().getObject().get("crumb").toString();
            } catch (UnirestException e) {
                // TODO do something
            }
        }
    }

    public String getRssAll() {
        try {
            HttpResponse<String> jsonResponse = Unirest.get(RSS_ALL)
                    .basicAuth(username, password)
                    .asString();
            return jsonResponse.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String fillJobNameAndDescription(int buildNumber, String reason, String description) {
        String sampleJson = "{\"displayName\":\"#" + buildNumber + " [" + reason + "]\", " +
                "\"description\": \"" + description + "\", " +
                "\"core:apply\": \"\", \"Jenkins-Crumb\": \"" + jenkinsCrumb + "\"}";
        try {
            HttpResponse<String> jsonResponse = Unirest.post(UPDATE_DESCRIPTION.replace("{id}", String.valueOf(buildNumber)))
                    .basicAuth(username, password)
                    .header("Jenkins-Crumb", jenkinsCrumb)
                    .field("json", sampleJson)
                    .asString();
            return jsonResponse.getStatusText();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return "";
    }
}
