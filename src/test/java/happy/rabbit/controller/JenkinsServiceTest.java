package happy.rabbit.controller;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.TestResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class JenkinsServiceTest {

    @Autowired
    private JenkinsService jenkinsService;

    @Test
    public void saveJob() {
        String jobName = "TestName";
        Job job = new Job();
        job.setDisplayName(jobName);
        jenkinsService.saveNewJob(job);
        assertThat(jenkinsService.getJobFromDB(jobName)).isNotNull();
    }

    @Test
    public void saveBuild() {
        Job job = getJob();
        jenkinsService.saveNewJob(job);
        Build build = new Build(job, 101L);
        jenkinsService.saveBuilds(Collections.singletonList(build));
        assertThat(jenkinsService.getBuild(build.getJob().getDisplayName(), build.getId())).isNotNull();
    }

    @Test
    public void testGettingJobs() {
        Job testJob = new Job();
        testJob.setDisplayName("Test");
        Job pipeline = new Job();
        pipeline.setDisplayName("TestPipeline");
        pipeline.setIsPipeline(true);
        pipeline.setTestJobs(Collections.singletonList(testJob));
        jenkinsService.saveNewJob(testJob);
        jenkinsService.saveNewJob(pipeline);
        assertThat(jenkinsService.getJobFromDB(pipeline.getDisplayName())).isNotNull();
        List<TestResult> testResults = jenkinsService.analyzeAndUpdateAllActivePipelines();
    }

    private Job getJob() {
        Job job = new Job();
        job.setDisplayName("TestName");
        return job;
    }

}