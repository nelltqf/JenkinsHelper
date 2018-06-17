package happy.rabbit.controller;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.statistics.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

import static happy.rabbit.utils.Utils.clean;

@RequestMapping("/")
@RestController
public class JenkinsController {

    private final JenkinsService jenkinsService;

    @Autowired
    public JenkinsController(JenkinsService jenkinsService) {
        this.jenkinsService = jenkinsService;
    }

    @RequestMapping(value = "cron",
            method = RequestMethod.GET)
    //Cron
    public void processAllActivePipelines() {
        jenkinsService.analyzeAndUpdateAllActivePipelines();
    }

    @RequestMapping(value = "addJob",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Job addNewJob(@RequestBody Job job) {
        return jenkinsService.saveNewJob(job);
    }

    @RequestMapping(value = "{jobName}/addTestJobs",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Job addTestJobs(@PathVariable String jobName, @RequestBody List<String> testJobs) {
        return jenkinsService.addTestJobs(clean(jobName), testJobs);
    }

    @RequestMapping(value = "{jobName}/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Build getBuildInformation(@PathVariable String jobName, @PathVariable String id) {
        return jenkinsService.getBuild(clean(jobName), id);
    }

    @RequestMapping(value = "all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllJobs() {
        return jenkinsService.getAllJobNames();
    }

    @RequestMapping(value = "{jobName}/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllBuild(@PathVariable String jobName) {
        return jenkinsService.getAllJobBuilds(clean(jobName));
    }

    @RequestMapping(value = "{jobName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Job getJob(@PathVariable String jobName) {
        return jenkinsService.getJobInformation(clean(jobName));
    }

    @RequestMapping(value = "saveJob",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Job saveJob(@RequestBody Job job) {
        return jenkinsService.saveNewJob(job);
    }

    @RequestMapping(value = "{jobName}/{ID}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateBuildDisplay(@PathVariable String jobName, @PathVariable String ID,
                                   @PathParam("failureReason") String failureReason,
                                   @PathParam("description") String description) {
        jenkinsService.updateBuildDisplay(jobName, ID, failureReason, description);
    }

    @RequestMapping(value = "{jobName}/stat",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Statistics getStatistics(@PathVariable String jobName) {
        return jenkinsService.getStatistics(clean(jobName));
    }
}
