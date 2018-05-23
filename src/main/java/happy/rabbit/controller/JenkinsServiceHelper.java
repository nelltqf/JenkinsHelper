package happy.rabbit.controller;

import happy.rabbit.data.Dao;
import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.TestResult;
import happy.rabbit.http.JenkinsApi;
import happy.rabbit.parser.Parser;
import happy.rabbit.utils.Utils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Job jobFromJenkins = getJobFromJenkins(jobName);
        Job jobFromDatabase = dao.getJob(jobName);
        if (jobFromDatabase == null) {
            dao.saveJob(jobFromJenkins);
            return jobFromJenkins;
        }
        Job job = mergeJob(jobFromDatabase, jobFromJenkins);
        collectErrors(job);
        return job;
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
                checkResponseStatusCode(testResponse, "Can't get test results from Jenkins: " + build);
                build.setTestResults(Parser.parseTests(Utils.httpResponseAsString(testResponse), build));
            });
        }
        return job;
    }

    private void checkResponseStatusCode(HttpResponse response, String errorMessage) {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            if (statusCode == 404) {
                return;
                // TODO handle situation with tech issues
            }
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
            fromDatabase.setTitle(build.getTitle());
        }
    }

    private Build findOrCreateBuild(Build build, Job jobFromDatabase) {
        Optional<Build> optional = jobFromDatabase.getBuilds()
                .stream()
                .filter(buildDB -> buildDB.getBuildId().equals(build.getBuildId()))
                .findAny();
        return optional.orElseGet(() -> dao.saveBuild(build));
    }

    public void updateBuildDisplay(String jobName, String id, String failureReason, String description) {
        jenkinsApi.fillJobNameAndDescription(jobName, id, failureReason, description);
    }

    public Job saveJob(Job job) {
        Job jobFromDatabase = dao.getJob(job.getDisplayName());
        if (jobFromDatabase == null) {
            dao.saveJob(job);
            return job;
        }
        jobFromDatabase.setTestJobs(job.getTestJobs());
        jobFromDatabase.setIsActive(job.isActive());
        return jobFromDatabase;
    }

    private void collectErrors(Job job) {
        if (!job.isPipeline()) {
            return;
        }
        List<Job> testJobs = job.getTestJobs();
        List<Build> pipelineRuns = job.getBuilds();

        pipelineRuns.forEach(pipelineRun -> {
            List<TestResult> testResults = getTestResultsForPipelineRun(pipelineRun, testJobs);
            pipelineRun.setTestResults(testResults);
        });
    }

    private List<TestResult> getTestResultsForPipelineRun(Build pipelineRun, List<Job> testJobs) {
        List<Build> testBuilds = new ArrayList<>();
        testJobs.forEach(testJob -> testBuilds.add(findMatchingBuild(pipelineRun, testJob)));

        List<TestResult> testResults = new ArrayList<>();
        testBuilds.forEach(testBuild -> testResults.addAll(testBuild.getTestResults()));
        return testResults;
    }

    private Build findMatchingBuild(Build pipelineRun, Job testJob) {
        return testJob.getBuilds()
                .stream()
                .filter(testBuild -> testBuild.getCauseBuild().getBuildId().equals(pipelineRun.getBuildId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find test build in job " + testJob
                        + "for pipeline run" + pipelineRun));
    }

}
