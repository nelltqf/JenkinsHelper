package happy.rabbit.data;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class HibernateDao implements Dao {

    private static final Logger LOGGER = Logger.getLogger(HibernateDao.class);

    @Autowired
    private org.hibernate.SessionFactory sessionFactory;

    public HibernateDao(org.hibernate.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Build getBuild(Build.BuildId buildId) {
        try {
            return (Build) getCurrentSession().get(Build.class, buildId);
        } catch (Exception e) {
            throw new IllegalStateException("Can't find build  " + buildId);
        }
    }

    @Override
    public Job getJob(String jobName) {
        try {
            return (Job) getCurrentSession().get(Job.class, jobName);
        } catch (Exception e) {
            throw new IllegalStateException("Can't find Job with name = " + jobName, e);
        }
    }

    @Override
    public Build saveOrUpdateBuild(Build build) {
        try {
            assert build.getId() != null;
            assert build.getJob() != null;

            Session session = getCurrentSession();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(build);
            transaction.commit();
        } catch (Exception e) {
            throw new IllegalStateException("Can't update Build for job " + build.getJob()
                    + " with number = " + build.getId(), e);
        }
        return build;
    }

    @Override
    public Job saveOrUpdateJob(Job job) {
        try {
            Session session = getCurrentSession();
            if (job.isPipeline()) {
                List<Build> allBuilds = new ArrayList<>(job.getBuilds());
                allBuilds.addAll(job.getTestJobs()
                        .stream()
                        .map(Job::getBuilds)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
                allBuilds.forEach(this::saveOrUpdateBuild);
            }
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(job);
            transaction.commit();
        } catch (Exception e) {
            throw new IllegalStateException("Can't update Job " + job.getDisplayName(), e);
        }
        return job;
    }

    @Override
    public List<Job> getAllJobs() {
        return getCurrentSession().createCriteria(Job.class).list();
    }

    private Session getCurrentSession() {
        try {
            return this.sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            LOGGER.warn("Reopening session", e);
            return this.sessionFactory.openSession();
        }
    }
}
