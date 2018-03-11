package happy.rabbit.http;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Test;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

import static happy.rabbit.utils.Utils.getJsonObjectFromJenkinsItem;

public class RestJenkinsApi implements JenkinsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestJenkinsApi.class);

    private static String jenkinsCrumb;

    private String baseUrl;
    private String username;
    private String password;

    public RestJenkinsApi(String baseUrl, String username, String password) {
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
                LOGGER.error("Exception during fetching crumb from Jenkins", e);
            }
        }
        return jenkinsCrumb;
    }

    public String getJobJson(String jobName) {
        try {
            return Request.get(baseUrl + JOB + jobName + GET_JOB_JSON)
                    .withBasicAuth(username, password)
                    .asString();
        } catch (Exception e) {
            LOGGER.error("Exception during getting job " + jobName + " from Jenkins", e);
            throw new IllegalStateException(e);
        }
    }

    public void fillJobNameAndDescription(Build build) {
        JSONObject jsonObject = getJsonObjectFromJenkinsItem(build);
        jsonObject.put("Jenkins-Crumb", jenkinsCrumb);
        try {
            Request.post(baseUrl + JOB + build.getJob().getDisplayName()
                    + UPDATE_DESCRIPTION.replace("{id}", String.valueOf(build.getNumber())))
                    .withBasicAuth(username, password)
                    .withHeader("Jenkins-Crumb", jenkinsCrumb)
                    .withFormField("json", jsonObject.toString())
                    .asString();
        } catch (Exception e) {
            LOGGER.error("Exception during getting build " + build + " from Jenkins", e);
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
