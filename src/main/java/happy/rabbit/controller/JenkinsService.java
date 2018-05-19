package happy.rabbit.controller;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;

import java.util.List;

public interface JenkinsService {

    Job getJobInformation(String jobName);

    void analyzeAndUpdateAllActivePipelines();

    Job saveNewJob(Job job);

    List<String> getAllJobNames();

    void updateBuildDisplay(String jobName, String id, String failureReason, String description);

    Job addTestJobs(String jobName, List<String> testJobs);

    Build getBuild(String jobName, String id);

    List<String> getAllJobBuilds(String jobName);
}
