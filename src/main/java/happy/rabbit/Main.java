package happy.rabbit;

import happy.rabbit.domain.JenkinsItem;
import happy.rabbit.http.NetworkServiceImpl;
import happy.rabbit.jenkins.JenkinsController;
import happy.rabbit.parser.JenkinsItemParser;
import happy.rabbit.statistics.StatisticsCollector;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static final JenkinsController controller = new JenkinsController();
    private static final JenkinsItemParser parser = new JenkinsItemParser();

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String csv = new String(Files.readAllBytes(Paths.get("src/main/resources/jenkins.csv")));
        List<JenkinsItem> jenkinsItems = parser.parseCsvToList(csv);
        controller.updateDescription(jenkinsItems);

        String rssAll = new NetworkServiceImpl().getRssAll();
        List<JenkinsItem> parsedJenkinsItems = parser.parseJsonToList(rssAll);

        StatisticsCollector collector = new StatisticsCollector(parsedJenkinsItems);
        collector.collect();
    }
}
