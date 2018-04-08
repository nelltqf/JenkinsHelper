package happy.rabbit.data;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class HibernateDao implements Dao {

    @Autowired
    private SessionFactory sessionFactory;

    public HibernateDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Build getBuild(String jobName, Long buildNumber) {
        Query query = getCurrentSession()
                .createQuery("from Build where number=:number and job_id=:job_id");
        query.setParameter("number", buildNumber);
        query.setParameter("job_id", jobName);
        return (Build) query.uniqueResult();
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
            session.saveOrUpdate(build);
            session.flush();
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
            session.flush();
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
            return this.sessionFactory.openSession();
        }
    }
}
