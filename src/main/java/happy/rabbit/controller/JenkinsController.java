package happy.rabbit.controller;

import happy.rabbit.data.BaseDao;
import happy.rabbit.domain.Build;
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
    private BaseDao<Build> baseDao;

    @Autowired
    private NetworkService networkService;

    @RequestMapping(value = "save",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveJenkinsItems(@RequestBody List<Build> jenkinsItems) {
        jenkinsItems.forEach(baseDao::saveOrUpdateItem);
    }

    @RequestMapping(value = "/{jobName}/{jobId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Build getItem(@PathVariable String jobName, @PathVariable Long jobId) {
        return baseDao.getItem(jobName, jobId);
    }

    @RequestMapping(value = "/{jobName}/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Build> getItemsForJob(@PathVariable String jobName) {
        return baseDao.getAllItems()
                .stream()
                .filter(item -> item.getJob() != null && item.getJob().getDisplayName().equals(jobName))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{jobName}/loadFromRss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Build> loadItemsFromJobRss(@PathVariable String jobName) {
//        List<Build> jenkinsItems = JobParser.parseJsonToListOfBuilds(networkService.getRssAll(jobName), jobName);
//        jenkinsItems.forEach(baseDao::saveOrUpdateItem);
//        return jenkinsItems;
        return null;
    }

    @RequestMapping(value = "/{jobName}/updateDescriptions",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateDescription(@PathVariable String jobName, @RequestBody List<Build> jenkinsItems) {
        jenkinsItems.forEach(item -> {
            baseDao.saveOrUpdateItem(item);
            networkService.fillJobNameAndDescription(item);
        });
    }

    @RequestMapping(value = "/errors",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    private Build collectErrors(@RequestBody Build item) {
        item.setErrors(networkService.getErrors(item));
        baseDao.saveOrUpdateItem(item);
        return item;
    }

    //Cron
    public void process() {
        List<String> jobs = baseDao.getListOfJobs();
        jobs.forEach(this::analyzeAndUpdate);
    }

    private void analyzeAndUpdate(String jobName) {
        List<Build> items = loadItemsFromJobRss(jobName);
        items.forEach(this::collectErrors);
//        Analyzer.assignTitleAndDescription(items);
        updateDescription(jobName, items);
    }
}
