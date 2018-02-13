package happy.rabbit.data;

import java.util.List;

public interface BaseDao<T> {

    T getItem(Long id);

    void deleteItem(Long id);

    T saveOrUpdateItem(T t);

    List<T> getAllItems();

}
