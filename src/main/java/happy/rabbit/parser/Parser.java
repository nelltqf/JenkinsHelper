package happy.rabbit.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static ObjectMapper customMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
}
