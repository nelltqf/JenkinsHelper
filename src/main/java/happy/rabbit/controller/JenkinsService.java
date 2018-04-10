package happy.rabbit.controller;

import happy.rabbit.data.Dao;
import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.Test;
import happy.rabbit.http.JenkinsApi;
import happy.rabbit.parser.Parser;
import happy.rabbit.utils.Utils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JenkinsService {

    private Dao dao;

    private JenkinsApi jenkinsApi;

    @Autowired
    public JenkinsService(@NotNull Dao dao, @NotNull JenkinsApi jenkinsApi) {
        this.dao = dao;
        this.jenkinsApi = jenkinsApi;
    }

    public void saveBuilds(List<Build> builds) {
        builds.forEach(dao::saveOrUpdateBuild);
    }

    public Build getBuild(String jobName, Long jobId) {
        Job buildJob = dao.getJob(jobName);
        return dao.getBuild(new Build().new BuildId(buildJob, jobId));
    }

    public Job getJobFromDB(String jobName) {
        return dao.getJob(jobName);
    }

    public Job loadJob(String jobName) {
        HttpResponse response = jenkinsApi.getJobJson(jobName);
        // TODO separate method
        if (response.getStatusLine().getStatusCode() != 200) {
            // TODO handle this
            throw new IllegalArgumentException("Can't get data from Jenkins");
        }
        Job jobFromJenkins = Parser.parseJob(Utils.httpResponseAsString(response));
        Job jobFromDB = dao.getJob(jobName);
        dao.saveBuilds(jobFromJenkins.getBuilds());
        // TODO investigate why it's not saved automatically
        jobFromDB.setBuilds(jobFromJenkins.getBuilds());
        return jobFromDB;
    }

    public void updateJenkins(List<Build> jenkinsItems, String jobName) {
        jenkinsItems.forEach(item -> {
            dao.saveOrUpdateBuild(item);
            jenkinsApi.fillJobNameAndDescription(item);
        });
    }

    public List<Test> getErrorsForPipelineRun(String jobName, Long id) {
        Job job = getJobFromDB(jobName);
        Build build = job.getBuilds().stream().filter(jobBuild -> jobBuild.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find any build with number " + id
                        + " for pipeline " + jobName));
//        item.setErrors(jenkinsApi.getErrors(item));
//        dao.saveOrUpdateBuilds(item);
//        return item;
        throw new NotImplementedException("Can't get errors from pipeline yet");
    }

    public void analyzeAndUpdateAllActivePipelines() {
        List<Job> jobs = dao.getAllJobs()
                .stream()
                .filter(Job::isActive)
                .collect(Collectors.toList());
        jobs.forEach(job -> loadJob(job.getDisplayName()));
        List<Build> failedBuilds = jobs.stream()
                .filter(Job::isPipeline)
                .map(Job::getBuilds)
                .flatMap(Collection::stream)
                .filter(Build::isBroken)
                .filter(build -> build.getFailureReason() == null || build.getFailureReason().isEmpty())
                .collect(Collectors.toList());

    }

    public List<Build> findTestBuildsForPipelineRun(Build pipelineBuild) {
        List<Build> testBuilds = new ArrayList<>();
        pipelineBuild.getJob().getTestJobs().forEach(testJob -> {
            Build testResultsBuild = testJob.getBuilds().stream()
                    .filter(testBuild -> pipelineBuild.getJob().getDisplayName().equals(testBuild.getCauseJobName()))
                    .filter(testBuild -> pipelineBuild.getId().equals(testBuild.getCauseNumber()))
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
