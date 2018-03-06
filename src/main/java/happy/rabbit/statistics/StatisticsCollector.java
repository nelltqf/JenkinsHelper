package happy.rabbit.statistics;

import happy.rabbit.domain.Build;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticsCollector {

    private List<Build> builds;

    public StatisticsCollector(List<Build> builds) {
        this.builds = builds;
    }

    public void collect() {
        List<Build> failedItems = builds.stream()
                .filter(Build::isBroken)
                .collect(Collectors.toList());
        int failedBuilds = failedItems.size();
        float failedBuildsPercent = failedBuilds / (float) builds.size() * 100;

        System.out.println("All failed: " + failedBuilds + " (" + failedBuildsPercent + "%)");
    }
}
