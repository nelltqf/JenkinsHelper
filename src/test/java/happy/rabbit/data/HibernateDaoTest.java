package happy.rabbit.data;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class HibernateDaoTest {

    @Autowired
    private Dao dao;

    @Test
    public void saveEmptyJob() {
        Job job = new Job("saveEmptyJob");
        dao.saveJob(job);

        assertThat(dao.getJob(job.getDisplayName())).isNotNull();
    }

    @Test
    public void saveJobWithBuild() {
        Job job = new Job("saveJobWithBuild");
        Build build = new Build(job, 1L);
        job.setBuilds(Collections.singletonList(build));
        dao.saveJob(job);

        Job fromDB = dao.getJob(job.getDisplayName());
        assertThat(fromDB).isNotNull();
        assertThat(fromDB.getBuilds()).isNotEmpty();
        assertThat(dao.getBuild(build.getBuildId())).isNotNull();
    }

    @Test
    public void saveJobSetBuildsLater() {
        Job job = new Job("saveJobSetBuildsLater");
        dao.saveJob(job);

        Build build = new Build(job, 1L);
        job.setBuilds(Collections.singletonList(build));

        assertThat(dao.getJob(job.getDisplayName()).getBuilds()).isNotEmpty();
    }

    @Test
    public void getAllJobs() {
        Job job1 = new Job("Test");
        Job job2 = new Job("Test2");

        dao.saveJob(job1);
        dao.saveJob(job2);

        assertThat(dao.getAllJobs().size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void savePipelineAndTestJob() {
        Job testJob = new Job("Test");
        Job pipeline = new Job("TestPipeline");

        pipeline.setTestJobs(Collections.singletonList(testJob));

        dao.saveJob(pipeline);

        assertThat(dao.getAllJobs().size()).isGreaterThanOrEqualTo(2);
    }

}