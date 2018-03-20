package happy.rabbit.controller;

import happy.rabbit.data.Dao;
import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.Test;
import happy.rabbit.http.JenkinsApi;
import happy.rabbit.parser.Parser;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JenkinsService {

    @Autowired
    private Dao dao;

    @Autowired
    private JenkinsApi jenkinsApi;


    public void saveBuilds(List<Build> builds) {
        builds.forEach(dao::saveOrUpdateBuild);
    }

    public Build getBuild(String jobName, Long jobId) {
        return dao.getBuild(jobName, jobId);
    }

    public Job getJobFromDB(String jobName) {
        return dao.getJob(jobName);
    }

    public Job loadJob(String jobName) {
        String json = jenkinsApi.getJobJson(jobName);
        return Parser.parseJson(json, Job.class);
    }

    public void updateJenkins(List<Build> jenkinsItems, String jobName) {
        jenkinsItems.forEach(item -> {
            dao.saveOrUpdateBuild(item);
            jenkinsApi.fillJobNameAndDescription(item);
        });
    }

    public List<Test> getErrorsForPipelineRun(String jobName, Long id) {
        Job job = getJobFromDB(jobName);
        Build build = job.getBuilds().stream().filter(jobBuild -> jobBuild.getNumber().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find any build with number " + id
                        + " for pipeline " + jobName));
//        item.setErrors(jenkinsApi.getErrors(item));
//        dao.saveOrUpdateBuilds(item);
//        return item;
        throw new NotImplementedException("Can't get errors from pipeline yet");
    }

    public void analyzeAndUpdateAllActivePipelines() {
        List<Job> jobs = dao.getAllJobs().stream()
                .filter(Job::isActive)
                .collect(Collectors.toList());
        jobs.forEach(job -> {
                    Job jobFromJenkins = Parser.parseJson(jenkinsApi.getJobJson(job.getDisplayName()), Job.class);
                    // TODO investigate if update is happening automatically here
                    dao.saveOrUpdateJob(jobFromJenkins);
                });
        Map<Job, List<Build>> failedBuilds = collectFailedBuilds(jobs);

        failedBuilds.forEach((job, builds) -> builds.forEach(build -> {
            List<Build> testBuilds = findTestBuildsForPipelineRun(build, job.getTestJobs());
            testBuilds.forEach(testBuild -> build.setTests(getErrorsFromBuild(testBuild)));
        }));
    }

    private Map<Job, List<Build>> collectFailedBuilds(List<Job> jobs) {
        Map<Job, List<Build>> map = new HashMap<>();
        jobs.stream()
                .filter(Job::isPipeline)
                .forEach(job -> {
                    List<Build> failedBuilds = job.getBuilds().stream()
                            .filter(Build::isBroken)
                            .filter(build -> build.getFailureReason() == null || build.getFailureReason().isEmpty())
                            .collect(Collectors.toList());
                    map.put(job, failedBuilds);
                });
        return map;
    }

    public List<Build> findTestBuildsForPipelineRun(Build pipelineBuild, List<Job> testJobs) {
        List<Build> testBuilds = new ArrayList<>();
        testJobs.forEach(testJob -> {
            Build testResultsBuild = testJob.getBuilds().stream()
                    .filter(testBuild -> pipelineBuild.getJob().getDisplayName().equals(testBuild.getCauseJobName()))
                    .filter(testBuild -> pipelineBuild.getNumber().equals(testBuild.getCauseNumber()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Can't find any build in "
                            + testJob.getDisplayName()
                            + "that was triggered by " + pipelineBuild));
            testBuilds.add(testResultsBuild);

        });
        return testBuilds;
    }

    private List<Test> getErrorsFromBuild(Build testBuild) {
        return Parser.parseTests(jenkinsApi.getErrors(testBuild));
    }

    private void analyzeAndUpdate(String jobName) {
//        List<Build> items = loadJobWithBuilds(jobName);
//        items.forEach(this::collectErrors);
//        Analyzer.assignTitleAndDescription(items);
//        updateDescription(jobName, items);
    }

    public Job saveNewJob(Job job) {
        dao.saveOrUpdateJob(job);
        return job;
    }
}
