package happy.rabbit.controller;

import happy.rabbit.data.BaseDao;
import happy.rabbit.domain.JenkinsItem;
import happy.rabbit.http.NetworkService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class JenkinsControllerTest {

    private static final String JOB_NAME = "someJob";
    private static final Long ID = 123L;
    @Mock
    private BaseDao baseDao;
    @Mock
    private NetworkService networkService;
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

    private List<JenkinsItem> listOfItems() {
        return new ArrayList<>(Arrays.asList(someJenkinsItem(), anotherJobJenkinsItem(1, 1)));
    }

    @Test
    public void testSavingItems() {
        JenkinsItem item = someJenkinsItem();
        List<JenkinsItem> items = new ArrayList<>();
        items.add(item);
        jenkinsController.saveJenkinsItems(items);
        Mockito.verify(baseDao).saveOrUpdateItem(item);
    }

    @Test
    public void testGettingItems() {
        jenkinsController.getItem(JOB_NAME, ID);
        Mockito.verify(baseDao).getItem(JOB_NAME, ID);
    }

    @Test
    public void testItemsForJob() {
        jenkinsController.getItemsForJob(JOB_NAME);
    }

    private JenkinsItem someJenkinsItem() {
        JenkinsItem item = new JenkinsItem();
        item.setItemJobId(JOB_NAME, ID);
        return null;
    }

    private JenkinsItem anotherJobJenkinsItem(int nextJob, int nextId) {
        JenkinsItem item = new JenkinsItem();
        item.setItemJobId(JOB_NAME + nextJob, ID + nextId);
        return null;
    }

}