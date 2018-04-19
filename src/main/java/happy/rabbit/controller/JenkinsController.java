package happy.rabbit.controller;

import happy.rabbit.domain.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value = "/addJob",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Job addNewJob(@RequestBody Job job) {
        return jenkinsService.saveNewJob(job);
    }

    @RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Job> getAllJobs() {
        return jenkinsService.getAllJobs();
    }

    @RequestMapping(value = "/{jobName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Job getJob(@PathVariable String jobName) {
        return jenkinsService.getJobInformation(jobName);
    }

    @RequestMapping(value = "/{jobName}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Job saveJob(@RequestBody Job job) {
        return jenkinsService.saveNewJob(job);
    }
}
