package happy.rabbit.parser;

import happy.rabbit.domain.Job;
import happy.rabbit.utils.Utils;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ParserTest {

    private static final String JOB_JSON = "src/test/resources/job.json";
    private static final String JOB_NAME = "TestPipeline";
    private static final String json = Utils.readFileToString(JOB_JSON);

    @Test
    public void parseJob() throws IOException {
        Job job = Parser.parseJson(json, Job.class);
        assertThat(job).hasNoNullFieldsOrPropertiesExcept("testJobs");
    }
    @Test
    public void parseJobName() throws IOException {
        Job job = Parser.parseJson(json, Job.class);
        assertThat(job.getDisplayName()).isEqualToIgnoringCase(JOB_NAME);
    }

    @Test
    public void parseJobBuilds() throws IOException {
        Job job = Parser.parseJson(json, Job.class);
        assertThat(job.getBuilds()).hasSize(6);
    }

    @Test
    public void parseError() throws IOException {
        assertThatCode(() -> Parser.parseJson("", Job.class))
                .hasNoSuppressedExceptions();

    }
}