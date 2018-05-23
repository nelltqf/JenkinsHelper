package happy.rabbit.statistics;

public class Statistics {

    private int builds;

    private int failedBuilds;

    private float successRate;

    private float failureRate;

    public int getBuilds() {
        return builds;
    }

    public void setBuilds(int builds) {
        this.builds = builds;
    }

    public int getFailedBuilds() {
        return failedBuilds;
    }

    public void setFailedBuilds(int failedBuilds) {
        this.failedBuilds = failedBuilds;
    }

    public float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(float successRate) {
        this.successRate = successRate;
    }

    public void setFailureRate(float failureRate) {
        this.failureRate = failureRate;
    }

    public float getFailureRate() {
        return failureRate;
    }
}
