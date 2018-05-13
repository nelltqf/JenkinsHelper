package happy.rabbit.controller;

import happy.rabbit.analyzer.Analyzer;
import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JenkinsService {

    private final JenkinsServiceHelper jenkinsServiceHelper;

    private final Analyzer analyzer;

    @Autowired
    public JenkinsService(JenkinsServiceHelper jenkinsServiceHelper, Analyzer analyzer) {
        this.jenkinsServiceHelper = jenkinsServiceHelper;
        this.analyzer = analyzer;
    }

    public Job getJobInformation(String jobName) {
        return jenkinsServiceHelper.getRefreshedJob(jobName);
    }

    public void analyzeAndUpdateAllActivePipelines() {
        List<Job> jobs = getAllActiveJobs();
        jobs.forEach(this::collectErrors);

        // Analise errors and set failure reason and description
        // Update failure reason and description on jenkins and in database
        jobs.stream()
                .filter(Job::isPipeline)
                .map(Job::getBuilds)
                .flatMap(Collection::stream)
                .forEach(build -> {
                    build.setFailureReason(analyzer.getFailureReason(build));
                    build.setDescription(analyzer.getDescription(build));
                    updateBuildDisplay(build.getJob().getDisplayName(),
                            String.valueOf(build.getId()),
                            build.getFailureReason(),
                            build.getDescription());
                });
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
                .filter(testBuild -> testBuild.getCauseBuild().equals(pipelineRun))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find test build in job " + testJob
                        + "for pipeline run" + pipelineRun));
    }

    public Job saveNewJob(Job job) {
        return job;
    }

    public List<Job> getAllJobs() {
        return jenkinsServiceHelper.getAllJobs();
    }

    private List<Job> getAllActiveJobs() {
        return jenkinsServiceHelper.getAllJobs()
                .stream()
                .filter(Job::isActive)
                .collect(Collectors.toList());
    }

    public void updateBuildDisplay(String jobName, String id, String failureReason, String description) {
        jenkinsServiceHelper.updateBuildDisplay(jobName, id, failureReason, description);
    }
}
