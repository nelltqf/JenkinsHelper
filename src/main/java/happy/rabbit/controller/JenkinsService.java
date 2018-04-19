package happy.rabbit.controller;

import happy.rabbit.data.Dao;
import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.TestResult;
import happy.rabbit.http.JenkinsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JenkinsService {

    private final JenkinsServiceHelper jenkinsServiceHelper;

    @Autowired
    public JenkinsService(@NotNull Dao dao, @NotNull JenkinsApi jenkinsApi, JenkinsServiceHelper jenkinsServiceHelper) {
        this.jenkinsServiceHelper = jenkinsServiceHelper;
    }

    public Job getJobInformation(String jobName) {
        return jenkinsServiceHelper.getRefreshedJob(jobName);
    }

    public void analyzeAndUpdateAllActivePipelines() {
        List<Job> jobs = getAllActiveJobs();
        // For each pipeline job
        // For each run
        // For each test job
        // Find matching build from test job
        // Get error list
        // Attach error list to run

        // Analise errors and set failure reason and description
        // Update failure reason and description on jenkins and in database
    }

    private List<TestResult> getErrorsFromBuild(Build testBuild) {
//        List<TestResult> testResults = Parser.parseTests(jenkinsApi.getErrors(testBuild));
//        Build causeBuild = testBuild.getCauseBuild();
//        if (causeBuild != null) {
//            causeBuild.setTestResults(testResults);
//            testResults.forEach(test -> test.setBuild(causeBuild));
//        }
//        return testResults;
        return null;
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
}
