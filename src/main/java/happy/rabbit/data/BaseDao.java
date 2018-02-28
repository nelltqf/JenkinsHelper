package happy.rabbit.data;

import java.util.List;

public interface BaseDao<T> {

    T getItem(String jobName, Long jobId);

    void deleteItem(Long id);

    T saveOrUpdateItem(T t);

    List<T> getAllItems();

    List<String> getListOfJobs();
}
