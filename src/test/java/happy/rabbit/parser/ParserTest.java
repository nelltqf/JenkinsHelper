package happy.rabbit.parser;

import happy.rabbit.domain.Job;
import happy.rabbit.domain.TestResult;
import org.junit.Test;

import java.util.List;

import static happy.rabbit.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ParserTest {

    @Test
    public void parseJob() {
        Job job = Parser.parseJob(JOB_JSON);
        assertThat(job).hasNoNullFieldsOrPropertiesExcept("testJobs");
    }

    @Test
    public void parseJobName() {
        Job job = Parser.parseJob(JOB_JSON);
        assertThat(job.getDisplayName()).isEqualToIgnoringCase(PIPELINE_NAME);
    }

    @Test
    public void parseJobBuilds() {
        Job job = Parser.parseJob(JOB_JSON);
        assertThat(job.getBuilds()).hasSize(6);
    }

    @Test
    public void parseTestJobBuilds() {
        Job job = Parser.parseJob(TEST_JOB_JSON);
        assertThat(job.getBuilds().get(0).getCauseJobName().getDisplayName()).isEqualTo("TestPipeline");
    }

    @Test
    public void parseTestResults() {
        List<TestResult> testResults = Parser.parseTests(TESTS_JSON);
        assertThat(testResults).isNotEmpty();
    }

    @Test
    public void parseError() {
        assertThatCode(() -> Parser.parseJson("", Job.class))
                .hasNoSuppressedExceptions();

    }
}