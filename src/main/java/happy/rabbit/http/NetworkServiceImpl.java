package happy.rabbit.http;

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

    public String fillJobNameAndDescription(int buildNumber, JSONObject jsonObject) {
        jsonObject.put("Jenkins-Crumb", jenkinsCrumb);
        try {
            return Request.post(UPDATE_DESCRIPTION.replace("{id}", String.valueOf(buildNumber)))
                    .withBasicAuth(username, password)
                    .withHeader("Jenkins-Crumb", jenkinsCrumb)
                    .withFormField("json", jsonObject.toString())
                    .asString();
        } catch (Exception e) {
            // TODO logging
            throw new IllegalStateException(e);
        }
    }
}
