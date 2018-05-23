package happy.rabbit.statistics;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.Job;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticsCollector {

    private Job job;

    private Statistics fullStatistics;

    public StatisticsCollector(Job job) {
        this.job = job;
    }

    public Statistics getFullStatistics() {
        if (fullStatistics == null) {
            List<Build> failedBuilds = job.getBuilds()
                    .stream()
                    .filter(Build::isBroken)
                    .collect(Collectors.toList());
            int totalNumberOfTests = job.getBuilds().size();
            float percentageOfFailures = (totalNumberOfTests - failedBuilds.size()) / (float) totalNumberOfTests;

            fullStatistics = StatisticsBuilder.allTime()
                    .withTotalNumberOfBuilds(totalNumberOfTests)
                    .withNumberOfFailedBuilds(failedBuilds.size())
                    .percentageOfFailures(percentageOfFailures)
                    .build();
        }
        return fullStatistics;
    }
}
