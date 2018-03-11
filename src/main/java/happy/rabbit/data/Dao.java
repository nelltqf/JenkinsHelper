package happy.rabbit.data;

import happy.rabbit.domain.Job;

import java.util.List;

public interface Dao<T> {

    T getItem(String jobName, Long jobId);

    void deleteItem(Long id);

    T saveOrUpdateItem(T t);

    List<T> getAllItems();

    List<String> getListOfJobs();

    Job getJob(String jobName);
}
