package happy.rabbit.domain;

public enum Status {
    REGRESSION(false),
    PASSED(true),
    FIXED(true),
    FAILED(false);

    private final boolean isPassed;

    Status(boolean isPassed) {
        this.isPassed = isPassed;
    }

    public boolean isPassed() {
        return isPassed;
    }
}
