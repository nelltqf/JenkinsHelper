package happy.rabbit.http;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Test;
import org.springframework.context.annotation.ImportResource;

import java.util.List;

@ImportResource(value = "environment.properties")
public interface NetworkService {

    String JOB = "job/";
    String GET_JOB_JSON = "/api/json";
    String UPDATE_DESCRIPTION = "{id}/configSubmit";
    String GET_CRUMB = "crumbIssuer/api/json";

    String getJobJson(String jobName);

    void fillJobNameAndDescription(Build jenkinsItem);

    List<Test> getErrors(Build jenkinsItem);

    Long findTestJobId(Build jenkinsItem);
}
