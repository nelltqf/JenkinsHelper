package happy.rabbit.jenkins;

public interface NetworkService {

    //TODO move to properties
    String BASE_URL = "http://localhost:49001/";
    String JOB = "job/TestJob/";

    String RSS_ALL = BASE_URL + JOB + "rssAll";
    String UPDATE_DESCRIPTION = BASE_URL + JOB + "{id}/configSubmit";
    String CRUMB = BASE_URL + "/crumbIssuer/api/json";

    // TODO hide
    String username = "admin";
    String password = "admin";

    String getRssAll();

    String fillJobNameAndDescription(int buildNumber, String reason, String description);
}
