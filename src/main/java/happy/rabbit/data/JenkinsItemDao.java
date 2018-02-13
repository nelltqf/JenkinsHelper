package happy.rabbit.data;

import happy.rabbit.domain.JenkinsItem;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JenkinsItemDao implements BaseDao<JenkinsItem> {

    @Autowired
    private SessionFactory sessionFactory;

    private Class thisClass = JenkinsItem.class;
    private String tableName = "jenkins_item";

    public JenkinsItemDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public JenkinsItem getItem(Long id) {
        try {
            return (JenkinsItem) getCurrentSession().get(thisClass, id);
        } catch (Exception e) {
            throw new IllegalStateException("Can't find JenkinsItem with id = " + id, e);
        }
    }

    @Override
    public void deleteItem(Long id) {
        try {
            if (checkIfIdExists(id)) {
                JenkinsItem member = (JenkinsItem) getCurrentSession().get(thisClass, id);
                getCurrentSession().delete(member);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete JenkinsItem with id=" + id, e);
        }
    }

    @Override
    public JenkinsItem saveOrUpdateItem(JenkinsItem jenkinsItem) {
        try {
            assert jenkinsItem.getId() != null;
            Session session = getCurrentSession();
            session.beginTransaction();
            session.saveOrUpdate(jenkinsItem);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new IllegalStateException("Can't update JenkinsItem with id=" + jenkinsItem.getId(), e);
        }
        getCurrentSession().flush();
        return jenkinsItem;
    }

    @Override
    public List<JenkinsItem> getAllItems() {
        return getCurrentSession().createQuery("from " + tableName).list();
    }

    private boolean checkIfIdExists(long id) {
        SQLQuery query = getCurrentSession()
                .createSQLQuery("select count(*) as result from :table where id = :id");
        query.setParameter("table", tableName).setParameter("id", id);
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
