package happy.rabbit.data;

import happy.rabbit.domain.*;

import java.util.List;

public interface Dao {

    Job getJob(String jobName);

    Build getBuild(BuildId buildId);

    Job saveJob(Job job);

    Build saveBuild(Build build);

    List<Job> getAllJobs();
}
