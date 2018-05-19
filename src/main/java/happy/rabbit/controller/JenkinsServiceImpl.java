package happy.rabbit.controller;

import happy.rabbit.analyzer.Analyzer;
import happy.rabbit.domain.Build;
import happy.rabbit.domain.BuildId;
import happy.rabbit.domain.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JenkinsServiceImpl implements JenkinsService {

    private final JenkinsServiceHelper jenkinsServiceHelper;

    private final Analyzer analyzer;

    @Autowired
    public JenkinsServiceImpl(JenkinsServiceHelper jenkinsServiceHelper, Analyzer analyzer) {
        this.jenkinsServiceHelper = jenkinsServiceHelper;
        this.analyzer = analyzer;
    }

    @Override
    public Job getJobInformation(String jobName) {
        return jenkinsServiceHelper.getRefreshedJob(jobName);
    }

    @Override
    public void analyzeAndUpdateAllActivePipelines() {
        List<Job> jobs = getAllActiveJobs();
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

    @Override
    public Job saveNewJob(Job job) {
        return jenkinsServiceHelper.saveJob(job);
    }

    @Override
    public List<String> getAllJobNames() {
        return jenkinsServiceHelper.getAllJobs()
                .stream()
                .filter(Job::isPipeline)
                .map(Job::getDisplayName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllJobBuilds(String jobName) {
        return jenkinsServiceHelper.getRefreshedJob(jobName)
                .getBuilds()
                .stream()
                .map(Build::getBuildId)
                .map(BuildId::toString)
                .collect(Collectors.toList());
    }

    private List<Job> getAllActiveJobs() {
        return jenkinsServiceHelper.getAllJobs()
                .stream()
                .filter(Job::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public void updateBuildDisplay(String jobName, String id, String failureReason, String description) {
        jenkinsServiceHelper.updateBuildDisplay(jobName, id, failureReason, description);
    }

    @Override
    public Job addTestJobs(String jobName, List<String> testJobs) {
        Job job = jenkinsServiceHelper.getRefreshedJob(jobName);
        List<Job> testJobsList = testJobs.stream()
                .map(jenkinsServiceHelper::getRefreshedJob)
                .collect(Collectors.toList());
        job.setTestJobs(testJobsList);
        return jenkinsServiceHelper.getRefreshedJob(job.getDisplayName());
    }

    @Override
    public Build getBuild(String jobName, String id) {
        return jenkinsServiceHelper.getRefreshedJob(jobName)
                .getBuilds()
                .stream()
                .filter(build -> build.getBuildId().getId().equals(Long.valueOf(id)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find build"));
    }
}
