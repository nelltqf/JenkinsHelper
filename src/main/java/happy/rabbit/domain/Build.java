package happy.rabbit.domain;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = "BUILD")
@Entity
public class Build {

    @EmbeddedId
    private BuildId id = new BuildId();

    private String description;

    private Result result;

    private String failureReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    /**
     * Stores duration in seconds
     */
    private Double duration;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testId.build")
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setTimestamp(Long timestamp) {
        this.date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                TimeZone.getDefault().toZoneId());;
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

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration/1000;
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
        return causeBuild == null ? null : causeBuild.getJob();
    }

    public Long getCauseNumber() {
        return causeBuild == null ? null : causeBuild.getId();
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

    public List<TestResult> showOnlyFailedTests() {
        return getTestResults()
                .stream()
                .filter(testResult -> !testResult.getStatus().isPassed())
                .collect(Collectors.toList());
    }
}
