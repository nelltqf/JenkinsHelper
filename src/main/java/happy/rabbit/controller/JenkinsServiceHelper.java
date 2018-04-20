package happy.rabbit.controller;

import happy.rabbit.data.Dao;
import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.http.JenkinsApi;
import happy.rabbit.parser.Parser;
import happy.rabbit.utils.Utils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JenkinsServiceHelper {

    private final Dao dao;

    private final JenkinsApi jenkinsApi;

    @Autowired
    public JenkinsServiceHelper(Dao dao, JenkinsApi jenkinsApi) {
        this.dao = dao;
        this.jenkinsApi = jenkinsApi;
    }

    public Job getRefreshedJob(String jobName) {
        Job jobFromDatabase = dao.getJob(jobName);
        Job jobFromJenkins = getJobFromJenkins(jobName);
        return mergeJob(jobFromDatabase, jobFromJenkins);
    }

    public List<Job> getAllJobs() {
        return dao.getAllJobs().stream()
                .map(job -> getRefreshedJob(job.getDisplayName()))
                .collect(Collectors.toList());
    }

    public Job getJobFromJenkins(String jobName) {
        HttpResponse response = jenkinsApi.getJobJson(jobName);
        checkResponseStatusCode(response, "Can't load job from Jenkins: " + jobName);
        Job job = Parser.parseJob(Utils.httpResponseAsString(response));
        if (!job.isPipeline()) {
            job.getBuilds().forEach(build -> {
                HttpResponse testResponse = jenkinsApi.getErrors(build);
                checkResponseStatusCode(response, "Can't get test results from Jenkins: " + build);
                build.setTestResults(Parser.parseTests(Utils.httpResponseAsString(response)));
            });
        }
        return job;
    }

    private void checkResponseStatusCode(HttpResponse response, String errorMessage) {
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private Job mergeJob(Job jobFromDatabase, Job jobFromJenkins) {
        jobFromJenkins.getBuilds().forEach(jenkinsBuild -> mergeBuild(jenkinsBuild, jobFromDatabase));
        return jobFromDatabase;
    }

    private void mergeBuild(Build build, Job jobFromDatabase) {
        // Maybe it worth just updating builds from Jenkins
        Build fromDatabase = findOrCreateBuild(build, jobFromDatabase);
        if (!fromDatabase.equals(build)) {
            // TODO investigate which failure reason and description has more priority
            fromDatabase.setDescription(build.getDescription());
            fromDatabase.setFailureReason(build.getFailureReason());
        }
    }

    private Build findOrCreateBuild(Build build, Job jobFromDatabase) {
        return jobFromDatabase.getBuilds()
                .stream()
                .filter(buildDB -> buildDB.getBuildId().equals(build.getBuildId()))
                .findAny()
                .orElse(dao.saveBuild(build));
    }
}
