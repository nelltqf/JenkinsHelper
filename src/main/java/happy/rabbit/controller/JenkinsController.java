package happy.rabbit.controller;

import happy.rabbit.data.BaseDao;
import happy.rabbit.domain.JenkinsItem;
import happy.rabbit.http.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/")
@RestController
public class JenkinsController {

    @Autowired
    private BaseDao<JenkinsItem> baseDao;

    @Autowired
    private NetworkService networkService;

    @RequestMapping(value = "/{jobName}/getAll",
            method = RequestMethod.GET)
    public List<JenkinsItem> saveJenkinsItems(@PathVariable String jobName) {
        return baseDao.getAllItems()
                .stream()
                .filter(item -> item.getJobName().equals(jobName))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "save",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveJenkinsItems(@RequestBody List<JenkinsItem> jenkinsItems) {
        jenkinsItems.forEach(baseDao::saveOrUpdateItem);
    }

    @RequestMapping(value = "updateDescriptions",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateDescription(@RequestBody List<JenkinsItem> jenkinsItems) {
        jenkinsItems.forEach(item -> {
            baseDao.saveOrUpdateItem(item);
            networkService.fillJobNameAndDescription(item.getId(), item);
        });
    }
}
