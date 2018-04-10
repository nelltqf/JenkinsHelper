package happy.rabbit.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "BUILD")
@Entity
public class Build {

    @EmbeddedId
    private BuildId id = new BuildId();

    private String description;

    private Result result;

    private String failureReason;

    private Long timestamp;

    /**
     * Stores duration in ms
     */
    private Long duration;

    @ManyToMany
    private List<Test> tests;
    private String causeJobName;
    private Long causeNumber;

    public Build() {

    }

    public Build(Job job, Long id) {
        this.id.setId(id);
        this.id.setJob(job);
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

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> errors) {
        this.tests = errors;
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

    public String getCauseJobName() {
        return causeJobName;
    }

    public Long getCauseNumber() {
        return causeNumber;
    }

    @Embeddable
    public class BuildId implements Serializable {

        @ManyToOne
        private Job job;

        private Long id;

        public BuildId() {
        }

        public BuildId(Job job, Long id) {
            this.job = job;
            this.id = id;
        }

        public Job getJob() {
            return job;
        }

        public void setJob(Job job) {
            this.job = job;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return job + " #" + id;
        }
    }
}
