package happy.rabbit.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import happy.rabbit.domain.FailureReason;
import happy.rabbit.domain.JenkinsItem;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JenkinsItemParser {

    private ObjectMapper mapper = customMapper();

    public List<JenkinsItem> parseJsonToList(String string) {
        try {
            List<JenkinsItem> jenkinsItems = new ArrayList<>();
            JSONArray jsonArray = prepareJsonArray(string);
            for (int i = 0; i < jsonArray.length(); i++) {
                jenkinsItems.add(mapper.readValue(jsonArray.get(i).toString(), JenkinsItem.class));
            }
            return jenkinsItems;
        } catch (Exception e) {
            // TODO logging
            throw new IllegalStateException(e);
        }
    }

    public List<JenkinsItem> parseCsvToList(String csv) {
        List<String> jenkinsItemsAsString = Arrays.asList(csv.split("\\n"));
        // TODO Set indexes from indexLine
        Iterator<String> iterator = jenkinsItemsAsString.iterator();
        String indexLine = iterator.next();
        List<JenkinsItem> jenkinsItems = new ArrayList<>();
        while (iterator.hasNext()) {
            jenkinsItems.add(parseOneCsvLine(iterator.next()));
        }
        return jenkinsItems;
    }

    private JenkinsItem parseOneCsvLine(String stringItem) {
        int idIndex = 0;
        int failureIndex = 1;
        int descriptionIndex = 2;
        String elements[] = stringItem.split(",");
        int id = Integer.parseInt(elements[idIndex]);
        FailureReason reason = FailureReason.valueOf(elements[failureIndex]);
        String description = elements[descriptionIndex];

        return new JenkinsItem(id, reason, description);
    }

    private JSONArray prepareJsonArray(String string) {
        JSONArray jsonArray = XML.toJSONObject(string).getJSONObject("feed").getJSONArray("entry");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            object.remove("link");

            String idString = (String) object.get("id");
            idString = idString.substring(idString.lastIndexOf(":") + 1, idString.length());
            object.put("id", idString);

            String fullName = (String) object.get("title");
            if (fullName.contains("[")) {
                object.put("failureReason", fullName.substring(fullName.indexOf('[') + 1, fullName.lastIndexOf(']')));
            }
            object.put("isBroken", !(fullName.contains("stable") || fullName.contains("normal")));
        }
        return jsonArray;
    }

    private ObjectMapper customMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
}
