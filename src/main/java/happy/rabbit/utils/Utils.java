package happy.rabbit.utils;

import happy.rabbit.domain.Build;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Utils {

    public static String getProperty(String propertyName) {
        if (System.getProperties().isEmpty()) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(new File("environment.properties")));
                System.setProperties(properties);
            } catch (IOException e) {
                throw new IllegalStateException("Can't read property file", e);
            }
        }
        return System.getProperty(propertyName);
    }

    public static JSONObject getJsonObjectFromJenkinsItem(Build item) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("displayName", "#" + item.getId() + " [" + item.getFailureReason() + "]");
        jsonObject.put("description", item.getDescription());
        jsonObject.put("core:apply", "");
        return jsonObject;
    }

    public static String readFileToString(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
