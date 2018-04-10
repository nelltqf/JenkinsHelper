package happy.rabbit.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import happy.rabbit.domain.Job;
import happy.rabbit.domain.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    private static final ObjectMapper CUSTOM_MAPPER = customMapper();

    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return CUSTOM_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            LOGGER.error("Error while parsing: ", e);
            throw new IllegalStateException(e);
        }
    }

    public static Job parseJob(String json) {
        Job job = parseJson(json, Job.class);
        job.getBuilds().forEach(build -> build.setJob(job));
        return job;
    }

    public static List<Test> parseTests(String json) {
        List<Test> tests = new ArrayList<>();
        try {
            JsonNode jsonNode = CUSTOM_MAPPER.readTree(new BufferedReader(new StringReader(json)));
            // TODO investigate how it works for multi-suites
            // TODO refactor
            jsonNode = jsonNode.get("childReports").findValue("suites");
            for (JsonNode node: jsonNode) {
                tests.addAll(CUSTOM_MAPPER.readValue(node.get("cases").toString(), new TypeReference<List<Test>>(){}));
            }
            return tests;
        } catch (IOException e) {
            LOGGER.error("Error while parsing: ", e);
            throw new IllegalStateException(e);
        }
    }

    private static ObjectMapper customMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
}
