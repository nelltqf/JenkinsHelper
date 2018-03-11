package happy.rabbit.controller;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/")
@RestController
public class JenkinsController {

    @Autowired
    private JenkinsService jenkinsService;

    @RequestMapping(value = "save",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveJenkinsBuilds(@RequestBody List<Build> jenkinsItems) {
        jenkinsService.saveBuilds(jenkinsItems);
    }

    @RequestMapping(value = "/{jobName}/{jobId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Build getItem(@PathVariable String jobName, @PathVariable Long jobId) {
        return jenkinsService.getBuild(jobName, jobId);
    }

    @RequestMapping(value = "/{jobName}/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Build> getBuildsForJob(@PathVariable String jobName) {
        return jenkinsService.getJob(jobName).getBuilds();
    }

    @RequestMapping(value = "/{jobName}/load",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Build> loadJobWithBuilds(@PathVariable String jobName) {
        Job job = jenkinsService.loadJob(jobName);
        return job.getBuilds();
    }

    @RequestMapping(value = "/{jobName}/updateDescriptions",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateDescription(@PathVariable String jobName, @RequestBody List<Build> jenkinsItems) {
        jenkinsService.updateJenkins(jenkinsItems, jobName);
    }

    @RequestMapping(value = "/{jobName}/{id}/errors",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    private List<Test> collectErrors(@PathVariable String jobName, @PathVariable Long id) {
        return jenkinsService.getErrorsForPipelineRun(jobName, id);
    }

    //Cron
    public void processAllActivePipelines() {
        jenkinsService.analyzeAndUpdateAllActivePipelines();
    }
}
