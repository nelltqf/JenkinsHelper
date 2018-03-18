package happy.rabbit;

import happy.rabbit.utils.Utils;

public class TestUtils {

    public static final String JOB_JSON_PATH = "src/test/resources/job.json";
    public static final String TEST_JSON_PATH = "src/test/resources/tests.json";

    public static final String JOB_JSON = Utils.readFileToString(JOB_JSON_PATH);
    public static final String TESTS_JSON = Utils.readFileToString(TEST_JSON_PATH);

    public static final String PIPELINE_NAME = "TestPipeline";

}
