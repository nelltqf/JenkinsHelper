package happy.rabbit.controller;

import happy.rabbit.data.Dao;
import happy.rabbit.domain.Build;
import happy.rabbit.domain.BuildId;
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
        return dao.getBuild(new BuildId(buildJob, jobId));
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
        List<BuildId> failedBuilds = jobs.stream()
                .filter(Job::isPipeline)
                .map(Job::getBuilds)
                .flatMap(Collection::stream)
                .filter(Build::isBroken)
//                .filter(build -> build.getFailureReason() == null || build.getFailureReason().isEmpty())
                .map(Build::getBuildId)
                .collect(Collectors.toList());
        List<Test> tests = jobs.stream()
                .filter(job -> !job.isPipeline())
                .map(Job::getBuilds)
                .flatMap(Collection::stream)
                .map(this::getErrorsFromBuild)
                .flatMap(Collection::stream)
                // TODO will work after overriding equals in BuildID
//                .filter(test -> test.getTestId().getBuild() != null
//                        && failedBuilds.contains(test.getTestId().getBuildId()))
                .collect(Collectors.toList());
        // TODO STUB
        System.out.println(tests);
    }

    private List<Test> getErrorsFromBuild(Build testBuild) {
        List<Test> tests = Parser.parseTests(jenkinsApi.getErrors(testBuild));
        testBuild.setTests(tests);
        tests.forEach(test -> test.setBuild(testBuild.getCauseBuild()));
        return tests;
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
