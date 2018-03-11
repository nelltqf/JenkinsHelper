package happy.rabbit.controller;

import happy.rabbit.data.Dao;
import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.Test;
import happy.rabbit.http.JenkinsApi;
import happy.rabbit.parser.Parser;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JenkinsService {

    @Autowired
    private Dao<Build> baseDao;

    @Autowired
    private JenkinsApi jenkinsApi;


    public void saveBuilds(List<Build> jenkinsItems) {
        jenkinsItems.forEach(baseDao::saveOrUpdateItem);
    }

    public Build getBuild(String jobName, Long jobId) {
        return baseDao.getItem(jobName, jobId);
    }

    public Job getJob(String jobName) {
        return baseDao.getJob(jobName);
    }

    public Job loadJob(String jobName) {
        String json = jenkinsApi.getJobJson(jobName);
        return Parser.parseJson(json, Job.class);
    }

    public void updateJenkins(List<Build> jenkinsItems, String jobName) {
        jenkinsItems.forEach(item -> {
            baseDao.saveOrUpdateItem(item);
            jenkinsApi.fillJobNameAndDescription(item);
        });
    }

    public List<Test> getErrorsForPipelineRun(String jobName, Long id) {
//        item.setErrors(jenkinsApi.getErrors(item));
//        baseDao.saveOrUpdateItem(item);
//        return item;
        throw new NotImplementedException("Can't get errors from pipeline yet");
    }

    public void analyzeAndUpdateAllActivePipelines() {
        // Get all available pipelines from DB
        // For each pipeline get test jobs
        // For each test job get builds with empty description/error list
        // For each build get errors from jenkinsApi
        // Try to analyze
        // Save to DB
        // Update descriptions
        throw new NotImplementedException("Can't process yet");
    }

    private void analyzeAndUpdate(String jobName) {
//        List<Build> items = loadJobWithBuilds(jobName);
//        items.forEach(this::collectErrors);
//        Analyzer.assignTitleAndDescription(items);
//        updateDescription(jobName, items);
    }
}
