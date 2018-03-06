package happy.rabbit.http;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Test;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

import static happy.rabbit.utils.Utils.getJsonObjectFromJenkinsItem;

public class NetworkServiceImpl implements NetworkService {

    private static String jenkinsCrumb;

    private String baseUrl;
    private String username;
    private String password;

    public NetworkServiceImpl(String baseUrl, String username, String password) {
        this.baseUrl = baseUrl + "/";
        this.username = username;
        this.password = password;
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

    public String getRssAll(String jobName) {
        try {
            return Request.get(baseUrl + JOB + jobName + GET_RSS_ALL)
                    .withBasicAuth(username, password)
                    .asString();
        } catch (Exception e) {
            // TODO logging
            throw new IllegalStateException(e);
        }
    }

    public void fillJobNameAndDescription(Build item) {
        JSONObject jsonObject = getJsonObjectFromJenkinsItem(item);
        jsonObject.put("Jenkins-Crumb", jenkinsCrumb);
        try {
            Request.post(baseUrl + JOB + item.getJob().getDisplayName()
                    + UPDATE_DESCRIPTION.replace("{id}", String.valueOf(item.getNumber())))
                    .withBasicAuth(username, password)
                    .withHeader("Jenkins-Crumb", jenkinsCrumb)
                    .withFormField("json", jsonObject.toString())
                    .asString();
        } catch (Exception e) {
            // TODO logging
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Test> getErrors(Build jenkinsItem) {
        throw new NotImplementedException();
    }

    @Override
    public Long findTestJobId(Build jenkinsItem) {
        throw new NotImplementedException();
    }
}
