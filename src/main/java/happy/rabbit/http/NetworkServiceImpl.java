package happy.rabbit.http;

import happy.rabbit.domain.JenkinsItem;
import org.json.JSONObject;

import static happy.rabbit.utils.Utils.getJsonObjectFromJenkinsItem;

public class NetworkServiceImpl implements NetworkService {

    private static String jenkinsCrumb;

    private String baseUrl;
    private String username;
    private String password;
    private String jobName;

    public NetworkServiceImpl(String baseUrl, String username, String password, String jobName) {
        this.baseUrl = baseUrl + "/";
        this.username = username;
        this.password = password;
        this.jobName = "job/" + jobName + "/";
        jenkinsCrumb = getJenkinsCrumb();
    }

    public String getJenkinsCrumb() {
        if (jenkinsCrumb == null) {
            try {
                JSONObject crumbJson = Request.get(baseUrl + GET_CRUMB)
                        .withBasicAuth(username, password)
                        .asJson();
                return crumbJson.get("crumb").toString();
            } catch (Exception e) {
                // TODO logging
//                throw new IllegalStateException(e);
            }
        }
        return jenkinsCrumb;
    }

    public String getRssAll() {
        try {
            return Request.get(baseUrl + jobName + GET_RSS_ALL)
                    .withBasicAuth(username, password)
                    .asString();
        } catch (Exception e) {
            // TODO logging
            throw new IllegalStateException(e);
        }
    }

    public void fillJobNameAndDescription(Long buildNumber, JenkinsItem jenkinsItem) {
        JSONObject jsonObject = getJsonObjectFromJenkinsItem(jenkinsItem);
        jsonObject.put("Jenkins-Crumb", jenkinsCrumb);
        try {
            Request.post(baseUrl + jobName + UPDATE_DESCRIPTION.replace("{id}", String.valueOf(buildNumber)))
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
