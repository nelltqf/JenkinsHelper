package happy.rabbit.data;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HibernateDao implements Dao<Build> {

    @Autowired
    private SessionFactory sessionFactory;

    private Class thisClass = Build.class;
    private String tableName = "build";

    public HibernateDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Build getItem(String jobName, Long id) {
        try {
            return (Build) getCurrentSession().get(thisClass, id);
        } catch (Exception e) {
            throw new IllegalStateException("Can't find Build with id = " + id, e);
        }
    }

    @Override
    public void deleteItem(Long id) {
        try {
            if (checkIfIdExists(id)) {
                Build member = (Build) getCurrentSession().get(thisClass, id);
                getCurrentSession().delete(member);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Can't delete Build with id=" + id, e);
        }
    }

    @Override
    public Build saveOrUpdateItem(Build jenkinsItem) {
        try {
            assert jenkinsItem.getNumber() != null;
            Session session = getCurrentSession();
            session.beginTransaction();
            session.saveOrUpdate(jenkinsItem);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new IllegalStateException("Can't update Build with number=" + jenkinsItem.getNumber(), e);
        }
        getCurrentSession().flush();
        return jenkinsItem;
    }

    @Override
    public List<Build> getAllItems() {
        return getCurrentSession().createQuery("from " + tableName).list();
    }

    @Override
    public List<String> getListOfJobs() {
        return getCurrentSession().createQuery("from jobs").list();
    }

    @Override
    public Job getJob(String jobName) {
        return null;
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
