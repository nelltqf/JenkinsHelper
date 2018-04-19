package happy.rabbit.data;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.BuildId;
import happy.rabbit.domain.Job;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

        hibernateUtil.getCurrentSession().saveOrUpdate(build);
        return build;
    }

    @Override
    public void saveBuilds(List<Build> builds) {
        builds.forEach(this::saveOrUpdateBuild);
    }

    @Override
    public Job saveOrUpdateJob(Job job) {
        hibernateUtil.getCurrentSession().saveOrUpdate(job);
        return job;
    }

    @Override
    public List<Job> getAllJobs() {
        return hibernateUtil.getCurrentSession().createCriteria(Job.class).list();
    }
}
