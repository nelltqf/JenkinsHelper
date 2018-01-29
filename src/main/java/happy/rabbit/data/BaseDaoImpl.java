package happy.rabbit.data;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseDaoImpl {

    @Autowired
    private SessionFactory sessionFactory;

    public BaseDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T getItem(Class<T> type, Long id) {
        try {
            return (T) getCurrentSession().get(type, id);
        } catch (Exception e) {
            throw new IllegalStateException("Can't get element of " + type + " with id=" + id);
        }
    }

    public <T> boolean createItem(T t) {
        Session session = getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(t);
        session.getTransaction().commit();
        return t != null;
    }

    public <T> void deleteItem(Class<T> type, Long id) {
        try {
            if (checkIfIdExists(id, type.getName().toLowerCase())) {
                T member = (T) getCurrentSession().get(type, id);
                getCurrentSession().delete(member);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete element of " + type + " with id=" + id);
        }
    }

    public <T> T updateItem(Class<T> type, Long id, T t) {
        try {
            if (!checkIfIdExists(id, type.getName().toLowerCase())) {
                return t;
            } else {
//                t.setId(id);
                getCurrentSession().update(t);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Can't update element of " + type + " with id=" + id);
        }
        getCurrentSession().flush();
        return t;
    }

    public <T> List<T> getAllItems(String table) {
        return getCurrentSession().createQuery("from " + table).list();
    }

    private boolean checkIfIdExists(long id, String table) {
        SQLQuery query = getCurrentSession()
                .createSQLQuery("select count(*) as result from :table where id = :id");
        query.setParameter("table", table).setParameter("id", id);
        query.addScalar("result");
        return !query.uniqueResult().toString().equals("0");

    }

    private Session getCurrentSession() {
        try {
            return this.sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            return this.sessionFactory.openSession();
        }
    }
}
