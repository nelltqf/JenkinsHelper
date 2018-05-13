package happy.rabbit.http;

import happy.rabbit.domain.Build;
import org.apache.http.HttpResponse;
import org.springframework.context.annotation.ImportResource;

@ImportResource(value = "environment.properties")
public interface JenkinsApi {

    String JOB = "job/";
    String GET_JOB_JSON = "/api/json?depth=1";
    String UPDATE_DESCRIPTION = "{id}/configSubmit";
    String GET_CRUMB = "crumbIssuer/api/json";
    String GET_ERRORS = "/testReport/api/json?depth=1";

    HttpResponse getJobJson(String jobName);

    void fillJobNameAndDescription(String jobName, String id, String title, String description);

    HttpResponse getErrors(Build jenkinsItem);

    Long findTestJobId(Build jenkinsItem);
}
