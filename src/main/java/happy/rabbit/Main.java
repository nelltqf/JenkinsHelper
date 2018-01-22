package happy.rabbit;

import happy.rabbit.domain.JenkinsItem;
import happy.rabbit.jenkins.NetworkServiceImpl;
import happy.rabbit.parser.JenkinsItemParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String rssAll = new NetworkServiceImpl().getRssAll();
        JenkinsItemParser parser = new JenkinsItemParser();
        List<JenkinsItem> jenkinsItems = parser.parseToList(rssAll);
        jenkinsItems.forEach(System.out::println);
    }
}
