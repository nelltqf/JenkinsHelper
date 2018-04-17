package happy.rabbit;

import happy.rabbit.utils.Utils;

public class TestUtils {

    public static final String RESOURCES_PATH = "src/test/resources/";

    public static final String JOB_JSON = Utils.readFileToString(RESOURCES_PATH + "job.json");
    public static final String TEST_JOB_JSON = Utils.readFileToString(RESOURCES_PATH + "testJob.json");
    public static final String TESTS_JSON = Utils.readFileToString(RESOURCES_PATH + "tests.json");

    public static final String PIPELINE_NAME = "TestPipeline";

}
