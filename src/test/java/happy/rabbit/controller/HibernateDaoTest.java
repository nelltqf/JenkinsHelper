package happy.rabbit.controller;

import happy.rabbit.data.Dao;
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
        Job job = getJob();
        dao.saveOrUpdateJob(job);

        assertThat(dao.getJob(job.getDisplayName())).isNotNull();
    }

    @Test
    public void saveJobWithBuild() {
        Job job = getJob();
        Build build = new Build(job, 1L);
        job.setBuilds(Collections.singletonList(build));
        dao.saveOrUpdateJob(job);

        Job fromDB = dao.getJob(job.getDisplayName());
        assertThat(fromDB).isNotNull();
        assertThat(fromDB.getBuilds()).isNotEmpty();
        assertThat(dao.getBuild(build.getBuildId())).isNotNull();
    }

    private Job getJob() {
        Job job = new Job();
        job.setDisplayName("TestName");
        return job;
    }

}