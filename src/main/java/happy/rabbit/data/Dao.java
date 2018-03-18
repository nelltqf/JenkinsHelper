package happy.rabbit.data;

import happy.rabbit.domain.*;

import java.util.List;

public interface Dao {

    Build getBuild(String jobName, Long buildNumber);

    Job getJob(String jobName);

    Build saveOrUpdateBuild(Build build);

    Job saveOrUpdateJob(Job job);

    List<Job> getAllJobs();
}
