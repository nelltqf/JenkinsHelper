package happy.rabbit.statistics;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.Result;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsCollectorTest {

    private StatisticsCollector statisticsCollector;

    private Job job;

    @Before
    public void beforeTest() {
        job = new Job("TestPipeline");
        statisticsCollector = new StatisticsCollector(job);
    }

    @Test
    public void testNumberOfBuilds() {
        Build build = new Build(job, 1L);
        job.getBuilds().add(build);
        assertThat(statisticsCollector.getFullStatistics().getBuilds()).isEqualTo(1);
    }

    @Test
    public void testFailedBuilds() {
        Build failed = new Build(job, 1L);
        failed.setResult(Result.FAILURE);
        Build successful = new Build(job, 1L);
        successful.setResult(Result.SUCCESS);
        job.getBuilds().add(failed);
        job.getBuilds().add(successful);
        assertThat(statisticsCollector.getFullStatistics().getFailedBuilds()).isEqualTo(1);
        assertThat(statisticsCollector.getFullStatistics().getSuccessRate()).isEqualTo(0.5f);
    }
}
