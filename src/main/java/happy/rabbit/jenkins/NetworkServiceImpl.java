package happy.rabbit.jenkins;


import happy.rabbit.http.Request;
import org.json.JSONObject;

public class NetworkServiceImpl implements NetworkService {

    private static String jenkinsCrumb;

    public NetworkServiceImpl() {
        if (jenkinsCrumb == null) {
            try {
                JSONObject crumbJson = Request.get(CRUMB)
                        .withBasicAuth(username, password)
                        .asJson();
                jenkinsCrumb = crumbJson.get("crumb").toString();
            } catch (Exception e) {
                // TODO logging
                throw new IllegalStateException(e);
            }
        }
    }

    public String getRssAll() {
        try {
            return Request.get(RSS_ALL)
                    .withBasicAuth(username, password)
                    .asString();
        } catch (Exception e) {
            // TODO logging
            throw new IllegalStateException(e);
        }
    }

    public String fillJobNameAndDescription(int buildNumber, String reason, String description) {
        String sampleJson = "{\"displayName\":\"#" + buildNumber + " [" + reason + "]\", " +
                "\"description\": \"" + description + "\", " +
                "\"core:apply\": \"\", \"Jenkins-Crumb\": \"" + jenkinsCrumb + "\"}";
        try {
            return Request.post(UPDATE_DESCRIPTION.replace("{id}", String.valueOf(buildNumber)))
                    .withBasicAuth(username, password)
                    .withHeader("Jenkins-Crumb", jenkinsCrumb)
                    .withFormField("json", sampleJson)
                    .asString();
        } catch (Exception e) {
            // TODO logging
            throw new IllegalStateException(e);
        }
    }
}
