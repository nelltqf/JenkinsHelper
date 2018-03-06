package happy.rabbit.parser;

import happy.rabbit.domain.Build;
import happy.rabbit.utils.Utils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JobParserTest {

    private static final String JOB_JSON = "src/test/resources/job.json";

    @Test
    public void assertNoBuildIsMissingAfterParsing() throws IOException {
        String json = Utils.readFileToString(JOB_JSON);
        List<Build> buildList = JobParser.parseJsonToListOfBuilds(json);
        assertThat(buildList).hasSize(6);
    }
}