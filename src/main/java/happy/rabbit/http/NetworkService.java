package happy.rabbit.http;

import happy.rabbit.domain.JenkinsItem;
import org.springframework.context.annotation.ImportResource;

@ImportResource(value = "environment.properties")
public interface NetworkService {

    String JOB = "job/";
    String GET_RSS_ALL = "/rssAll";
    String UPDATE_DESCRIPTION = "{id}/configSubmit";
    String GET_CRUMB = "crumbIssuer/api/json";

    String getRssAll(String jobName);

    void fillJobNameAndDescription(Long buildNumber, JenkinsItem jenkinsItem, String jobName);
}
