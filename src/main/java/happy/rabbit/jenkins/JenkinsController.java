package happy.rabbit.jenkins;

import happy.rabbit.data.BaseDao;
import happy.rabbit.domain.JenkinsItem;
import happy.rabbit.http.NetworkService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public class JenkinsController {

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private NetworkService networkService;

    @RequestMapping("updateDescriptions")
    public void updateDescription(@RequestBody List<JenkinsItem> jenkinsItems) {
        jenkinsItems.forEach(this::updateDescription);
    }

    @RequestMapping("updateDescription")
    public void updateDescription(@RequestBody JenkinsItem item) {
        assert item.getId() != null;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("displayName", "#" + item.getId() + " [" + item.getFailureReason() + "]");
        jsonObject.put("description", item.getContent());
        jsonObject.put("core:apply", "");
        baseDao.updateItem(JenkinsItem.class, item.getId(), item);
        networkService.fillJobNameAndDescription(item.getId(), jsonObject);
    }

}
