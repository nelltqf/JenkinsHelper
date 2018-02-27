package happy.rabbit.controller;

import happy.rabbit.data.BaseDao;
import happy.rabbit.http.NetworkService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JenkinsControllerTest {

    @Mock
    private BaseDao baseDao;

    @Mock
    private NetworkService networkService;

    @InjectMocks
    private JenkinsController jenkinsController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSavingItems() {
//        List<JenkinsItem> items = new ArrayList<>();
//        Mockito.doReturn(null)
//                .when(baseDao)
//                .getItem(Mockito.any());
//        items.add(new JenkinsItem());
//        jenkinsController.saveJenkinsItems(items);
    }

}