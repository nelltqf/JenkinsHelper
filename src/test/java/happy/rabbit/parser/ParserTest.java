package happy.rabbit.parser;

import happy.rabbit.domain.Job;
import org.junit.Test;

import java.util.List;

import static happy.rabbit.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ParserTest {

    @Test
    public void parseJob() {
        Job job = Parser.parseJson(JOB_JSON, Job.class);
        assertThat(job).hasNoNullFieldsOrPropertiesExcept("testJobs");
    }

    @Test
    public void parseJobName() {
        Job job = Parser.parseJson(JOB_JSON, Job.class);
        assertThat(job.getDisplayName()).isEqualToIgnoringCase(PIPELINE_NAME);
    }

    @Test
    public void parseJobBuilds() {
        Job job = Parser.parseJson(JOB_JSON, Job.class);
        assertThat(job.getBuilds()).hasSize(6);
    }

    @Test
    public void parseTestResults() {
        List<happy.rabbit.domain.Test> tests = Parser.parseTests(TESTS_JSON);
        assertThat(tests).isNotEmpty();
    }

    @Test
    public void parseError() {
        assertThatCode(() -> Parser.parseJson("", Job.class))
                .hasNoSuppressedExceptions();

    }
}