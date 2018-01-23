package happy.rabbit.statistics;

import happy.rabbit.domain.JenkinsItem;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticsCollector {

    private List<JenkinsItem> jenkinsItems;

    public StatisticsCollector(List<JenkinsItem> jenkinsItems) {
        this.jenkinsItems = jenkinsItems;
    }

    public void collect() {
        List<JenkinsItem> failedItems = jenkinsItems.stream()
                .filter(JenkinsItem::isBroken)
                .collect(Collectors.toList());
        int failedBuilds = failedItems.size();
        float failedBuildsPercent = failedBuilds / (float) jenkinsItems.size() * 100;

        System.out.println("All failed: " + failedBuilds + " (" + failedBuildsPercent + "%)");
    }
}
