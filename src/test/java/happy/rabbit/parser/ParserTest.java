package happy.rabbit.parser;

import happy.rabbit.domain.Job;
import happy.rabbit.utils.Utils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ParserTest {

    private static final String JOB_JSON_PATH = "src/test/resources/job.json";
    private static final String TEST_JSON_PATH = "src/test/resources/tests.json";
    private static final String JOB_NAME = "TestPipeline";
    private static final String JOB_JSON = Utils.readFileToString(JOB_JSON_PATH);
    private static final String TESTS_JSON = Utils.readFileToString(TEST_JSON_PATH);

    @Test
    public void parseJob() throws IOException {
        Job job = Parser.parseJson(JOB_JSON, Job.class);
        assertThat(job).hasNoNullFieldsOrPropertiesExcept("testJobs");
    }
    @Test
    public void parseJobName() throws IOException {
        Job job = Parser.parseJson(JOB_JSON, Job.class);
        assertThat(job.getDisplayName()).isEqualToIgnoringCase(JOB_NAME);
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