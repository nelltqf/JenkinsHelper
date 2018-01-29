package happy.rabbit.data;

import java.util.List;

public interface BaseDao {

    <T> T getItem(Class<T> type, Long id);

    <T> boolean createItem(T t);

    <T> void deleteItem(Class<T> type, Long id);

    <T> T updateItem(Class<T> type, Long id, T t);

    <T> List<T> getAllItems(String table);

}
