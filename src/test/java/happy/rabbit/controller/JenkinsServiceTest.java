package happy.rabbit.controller;

import happy.rabbit.data.Dao;
import happy.rabbit.http.JenkinsApi;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static happy.rabbit.TestUtils.JOB_JSON;
import static happy.rabbit.TestUtils.PIPELINE_NAME;

public class JenkinsServiceTest {

    @Mock
    private Dao dao;

    @Mock
    private JenkinsApi jenkinsApi;

    @InjectMocks
    private JenkinsService jenkinsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(jenkinsApi.getJobJson(PIPELINE_NAME)).thenReturn(JOB_JSON);
    }

    @Test
    public void testGettingJobs() {
        jenkinsService.analyzeAndUpdateAllActivePipelines();
    }

}