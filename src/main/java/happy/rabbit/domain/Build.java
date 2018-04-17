package happy.rabbit.domain;

import javax.persistence.*;
import java.util.List;

@Table(name = "BUILD")
@Entity
public class Build {

    @EmbeddedId
    private BuildId id = new BuildId();

    private String description;

    private Result result;

    private String failureReason;

    // TODO make real date
    private Long timestamp;

    /**
     * Stores duration in ms
     */
    private Long duration;

    @OneToMany(mappedBy = "testId.build")
    private List<TestResult> testResults;

    @OneToOne
    private Build causeBuild;

    public Build() {

    }

    public Build(Job job, Long id) {
        this.id.setId(id);
        this.id.setJob(job);
    }

    public Build(BuildId causeId) {
        this.id.setId(causeId.getId());
        this.id.setJob(causeId.getJob());
    }

    public BuildId getBuildId() {
        return id;
    }

    public Long getId() {
        return id.getId();
    }

    public void setId(Long id) {
        this.id.setId(id);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long published) {
        this.timestamp = published;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String content) {
        this.description = content;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Job getJob() {
        return id.getJob();
    }

    public void setJob(Job job) {
        this.id.setJob(job);
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> errors) {
        this.testResults = errors;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void setDisplayName(String displayName) {
        if (displayName.contains("[") && displayName.contains("]")) {
            this.failureReason = displayName.substring(displayName.indexOf('[') + 1, displayName.lastIndexOf(']'));
        }
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public boolean isBroken() {
        return result == Result.FAILURE;
    }

    public Job getCauseJobName() {
        return causeBuild.getJob();
    }

    public Long getCauseNumber() {
        return causeBuild.getId();
    }

    public void setCauseId(BuildId causeId) {
        this.causeBuild = new Build(causeId);
    }

    public Build getCauseBuild() {
        return causeBuild;
    }

    public void setCauseBuild(Build causeBuild) {
        this.causeBuild = causeBuild;
    }
}
