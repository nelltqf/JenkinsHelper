package happy.rabbit.parser;

import happy.rabbit.domain.Job;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static happy.rabbit.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ParserTest {

    @Test
    public void parseJob() throws IOException {
        Job job = Parser.parseJson(JOB_JSON, Job.class);
        assertThat(job).hasNoNullFieldsOrPropertiesExcept("testJobs");
    }
    @Test
    public void parseJobName() throws IOException {
        Job job = Parser.parseJson(JOB_JSON, Job.class);
        assertThat(job.getDisplayName()).isEqualToIgnoringCase(PIPELINE_NAME);
    }

    @Test
    public void parseJobBuilds() throws IOException {
        Job job = Parser.parseJson(JOB_JSON, Job.class);
        assertThat(job.getBuilds()).hasSize(6);
    }

    @Test
    public void parseTestResults() throws IOException {
        List<happy.rabbit.domain.Test> tests = Parser.parseTests(TESTS_JSON);
        assertThat(tests).isNotEmpty();
    }

    @Test
    public void parseError() throws IOException {
        assertThatCode(() -> Parser.parseJson("", Job.class))
                .hasNoSuppressedExceptions();

    }
}