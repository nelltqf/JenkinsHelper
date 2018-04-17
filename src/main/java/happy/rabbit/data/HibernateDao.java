package happy.rabbit.data;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.BuildId;
import happy.rabbit.domain.Job;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Component
public class HibernateDao implements Dao {

    private static final Logger LOGGER = Logger.getLogger(HibernateDao.class);

    @Autowired
    private HibernateUtil hibernateUtil;

    @Autowired
    public HibernateDao(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Build getBuild(BuildId buildId) {
        try {
            return (Build) hibernateUtil.getCurrentSession().get(Build.class, buildId);
        } catch (Exception e) {
            throw new IllegalStateException("Can't find build  " + buildId);
        }
    }

    @Override
    public Job getJob(String jobName) {
        try {
            return (Job) hibernateUtil.getCurrentSession().get(Job.class, jobName);
        } catch (Exception e) {
            throw new IllegalStateException("Can't find Job with name = " + jobName, e);
        }
    }

    @Override
    public Build saveOrUpdateBuild(Build build) {
        assert build.getId() != null;
        assert build.getJob() != null;

        Session session = hibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            // TODO pain guts pan
            if (build.getCauseBuild() != null) {
                Build cause = getBuild(build.getCauseBuild().getBuildId());
                if (cause != null) {
                    build.setCauseBuild(cause);
                } else {
                    session.saveOrUpdate(build.getCauseBuild());
                }
            }
            session.saveOrUpdate(build);
            transaction.commit();
        } catch (NonUniqueObjectException e) {
            // TODO prevent this
            transaction.rollback();
        } catch (Exception e) {
            throw new IllegalStateException("Can't update Build for job " + build.getJob()
                    + " with number = " + build.getId(), e);
        }
        return build;
    }

    @Override
    public void saveBuilds(List<Build> builds) {
        builds.forEach(this::saveOrUpdateBuild);
    }

    @Override
    public Job saveOrUpdateJob(Job job) {
        try {
            Session session = hibernateUtil.getCurrentSession();
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
        return hibernateUtil.getCurrentSession().createCriteria(Job.class).list();
    }
}
