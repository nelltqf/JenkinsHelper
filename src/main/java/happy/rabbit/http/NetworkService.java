package happy.rabbit.http;

import happy.rabbit.domain.JenkinsItem;
import happy.rabbit.domain.Test;
import org.springframework.context.annotation.ImportResource;

import java.util.List;

@ImportResource(value = "environment.properties")
public interface NetworkService {

    String JOB = "job/";
    String GET_RSS_ALL = "/rssAll";
    String UPDATE_DESCRIPTION = "{id}/configSubmit";
    String GET_CRUMB = "crumbIssuer/api/json";

    String getRssAll(String jobName);

    void fillJobNameAndDescription(JenkinsItem jenkinsItem);

    List<Test> getErrors(JenkinsItem jenkinsItem);

    Long findTestJobId(JenkinsItem jenkinsItem);
}
