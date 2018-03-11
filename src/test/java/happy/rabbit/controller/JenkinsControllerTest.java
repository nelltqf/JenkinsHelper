package happy.rabbit.controller;

import happy.rabbit.data.Dao;
import happy.rabbit.domain.Build;
import happy.rabbit.http.JenkinsApi;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class JenkinsControllerTest {

    private static final String JOB_NAME = "someJob";
    private static final Long ID = 123L;
    @Mock
    private Dao baseDao;
    @Mock
    private JenkinsApi jenkinsApi;
    @InjectMocks
    private JenkinsController jenkinsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito.doReturn(someJenkinsItem())
                .when(baseDao)
                .getItem(Mockito.eq(JOB_NAME), Mockito.eq(ID));

        Mockito.doReturn(listOfItems())
                .when(baseDao)
                .getAllItems();
    }

    private List<Build> listOfItems() {
        return new ArrayList<>(Arrays.asList(someJenkinsItem(), anotherJobJenkinsItem(1, 1)));
    }

    @Test
    public void testSavingItems() {
        Build item = someJenkinsItem();
        List<Build> items = new ArrayList<>();
        items.add(item);
        jenkinsController.saveJenkinsBuilds(items);
        Mockito.verify(baseDao).saveOrUpdateItem(item);
        assertThat(baseDao.getItem(item.getJob().getDisplayName(), item.getNumber()))
                .isEqualToComparingFieldByField(item);
    }

    @Test
    public void testGettingItems() {
        jenkinsController.getItem(JOB_NAME, ID);
        Mockito.verify(baseDao).getItem(JOB_NAME, ID);
    }

    @Test
    public void testItemsForJob() {
        jenkinsController.getBuildsForJob(JOB_NAME);
    }

    private Build someJenkinsItem() {
        return new Build(JOB_NAME, ID);
    }

    private Build anotherJobJenkinsItem(int nextJob, int nextId) {
        return new Build(JOB_NAME + nextJob, ID + nextId);
    }

}