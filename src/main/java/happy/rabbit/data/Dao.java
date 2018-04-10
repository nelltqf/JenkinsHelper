package happy.rabbit.data;

import happy.rabbit.domain.*;

import java.util.List;

public interface Dao {

    Build getBuild(Build.BuildId buildId);

    Job getJob(String jobName);

    Build saveOrUpdateBuild(Build build);

    void saveBuilds(List<Build> builds);

    Job saveOrUpdateJob(Job job);

    List<Job> getAllJobs();
}
