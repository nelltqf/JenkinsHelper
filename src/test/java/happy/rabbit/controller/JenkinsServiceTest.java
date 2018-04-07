package happy.rabbit.controller;

import happy.rabbit.domain.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class JenkinsServiceTest {

    @Autowired
    private JenkinsService jenkinsService;

    @Test
    public void saveJob() {
        Job job = new Job();
        job.setDisplayName("TestName");
        jenkinsService.saveNewJob(job);
        assertThat(jenkinsService.getJobFromDB("Test")).isNotNull();
    }

    @Test
    public void testGettingJobs() {
        jenkinsService.analyzeAndUpdateAllActivePipelines();
    }

}