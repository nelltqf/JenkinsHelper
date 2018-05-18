package happy.rabbit.controller;

import happy.rabbit.domain.Job;

import java.util.List;

public interface JenkinsService {

    Job getJobInformation(String jobName);

    void analyzeAndUpdateAllActivePipelines();

    Job saveNewJob(Job job);

    List<Job> getAllJobs();

    void updateBuildDisplay(String jobName, String id, String failureReason, String description);
}
