package happy.rabbit.jenkins;

import happy.rabbit.domain.JenkinsItem;
import happy.rabbit.http.NetworkService;
import happy.rabbit.http.NetworkServiceImpl;
import org.json.JSONObject;

import java.util.List;

public class JenkinsController {

    private NetworkService networkService = new NetworkServiceImpl();

    public void updateDescription(List<JenkinsItem> jenkinsItems) {
        jenkinsItems.forEach(this::updateDescription);
    }

    public void updateDescription(JenkinsItem item) {
        assert item.getId() != null;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("displayName", "#" + item.getId() + " [" + item.getFailureReason() + "]");
        jsonObject.put("description", item.getContent());
        jsonObject.put("core:apply", "");
        networkService.fillJobNameAndDescription(item.getId(), jsonObject);
    }

}
