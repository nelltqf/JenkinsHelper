package happy.rabbit.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Build {

    @Id
    private Long id;

    private Long number;

    @ManyToOne
    private Job job;

    private String description;

    private Result result;

    private String failureReason;

    private Long timestamp;

    /**
     * Stores duration in ms
     */
    private Long duration;

    @OneToMany
    private List<Test> errors;

    public Build() {

    }

    public Build(String jobName, Long id) {
        this.job = new Job(jobName);
        this.number = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
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
        return job;
    }

    public void setJob(String jobName) {
        this.job = new Job(jobName);
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<Test> getErrors() {
        return errors;
    }

    public void setErrors(List<Test> errors) {
        this.errors = errors;
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
        return "#" + number;
    }

    public boolean isBroken() {
        return result == Result.FAILURE;
    }
}
