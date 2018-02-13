package happy.rabbit.controller;

import happy.rabbit.data.BaseDao;
import happy.rabbit.domain.JenkinsItem;
import happy.rabbit.http.NetworkService;
import happy.rabbit.parser.JenkinsItemParser;
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
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JenkinsItem> getItemsForJob(@PathVariable String jobName) {
        return baseDao.getAllItems()
                .stream()
                .filter(item -> item.getJobName().equals(jobName))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{jobName}/loadFromRss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JenkinsItem> loadItemsFromJobRss(@PathVariable String jobName) {
        List<JenkinsItem> jenkinsItems = JenkinsItemParser.parseJsonToList(networkService.getRssAll(jobName), jobName);
        jenkinsItems.forEach(baseDao::saveOrUpdateItem);
        return jenkinsItems;
    }

    @RequestMapping(value = "save",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveJenkinsItems(@RequestBody List<JenkinsItem> jenkinsItems) {
        jenkinsItems.forEach(baseDao::saveOrUpdateItem);
    }

    @RequestMapping(value = "/{jobName}/updateDescriptions",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateDescription(@PathVariable String jobName, @RequestBody List<JenkinsItem> jenkinsItems) {
        jenkinsItems.forEach(item -> {
            baseDao.saveOrUpdateItem(item);
            networkService.fillJobNameAndDescription(item.getId(), item, jobName);
        });
    }
}
