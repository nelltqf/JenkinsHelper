package happy.rabbit.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class JenkinsServiceTest {

    @Autowired
    private JenkinsService jenkinsService;

    /**
     * Temp test to avoid starting server to check real-life functionality
     */
    @Test
    public void testCron() {
        jenkinsService.analyzeAndUpdateAllActivePipelines();
    }

}