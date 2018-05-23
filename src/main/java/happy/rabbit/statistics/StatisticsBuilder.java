package happy.rabbit.statistics;

public class StatisticsBuilder {

    private Statistics statistics = new Statistics();

    public static StatisticsBuilder allTime() {
        return new StatisticsBuilder();
    }

    public Statistics build() {
        return statistics;
    }

    public StatisticsBuilder withTotalNumberOfBuilds(int numberOfBuilds) {
        statistics.setBuilds(numberOfBuilds);
        return this;
    }

    public StatisticsBuilder withNumberOfFailedBuilds(int numberOfBuilds) {
        statistics.setFailedBuilds(numberOfBuilds);
        return this;
    }

    public StatisticsBuilder percentageOfFailures(float percentageOfFailures) {
        statistics.setSuccessRate(percentageOfFailures);
        statistics.setFailureRate(1 - percentageOfFailures);
        return this;
    }
}
