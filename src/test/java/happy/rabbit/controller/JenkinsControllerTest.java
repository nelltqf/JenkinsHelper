package happy.rabbit.controller;

import happy.rabbit.domain.Build;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class JenkinsControllerTest {

    private static final String JOB_NAME = "someJob";
    private static final Long ID = 123L;

    @InjectMocks
    private JenkinsController jenkinsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private List<Build> listOfItems() {
        return new ArrayList<>(Arrays.asList(someJenkinsItem(), anotherJobJenkinsItem(1, 1)));
    }

    private Build someJenkinsItem() {
        return new Build(JOB_NAME, ID);
    }

    private Build anotherJobJenkinsItem(int nextJob, int nextId) {
        return new Build(JOB_NAME + nextJob, ID + nextId);
    }

}