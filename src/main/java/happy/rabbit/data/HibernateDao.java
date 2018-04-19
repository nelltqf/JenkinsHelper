package happy.rabbit.data;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.BuildId;
import happy.rabbit.domain.Job;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
@Transactional
public class HibernateDao implements Dao {

    private static final Logger LOGGER = Logger.getLogger(HibernateDao.class);

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Override
    public Job getJob(String jobName) {
        return entityManager.find(Job.class, jobName);
    }

    @Override
    public Build getBuild(BuildId buildId) {
        return entityManager.find(Build.class, buildId);
    }

    @Override
    public Job saveJob(Job job) {
        entityManager.persist(job);
        return job;
    }

    @Override
    public Build saveBuild(Build build) {
        entityManager.persist(build);
        return build;
    }

    @Override
    public List<Job> getAllJobs() {
        return entityManager.createQuery("SELECT job from Job job").getResultList();
    }
}
