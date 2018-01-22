package happy.rabbit.jenkins;


import happy.rabbit.http.Request;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
