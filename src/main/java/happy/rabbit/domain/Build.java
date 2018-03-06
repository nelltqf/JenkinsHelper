package happy.rabbit.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "build")
@Table(name = "build")
public class Build {

    @Id
    private Long number;

    private Long id;

    private FailureReason failureReason;

    private String content;

    private Long testJobId;

    private boolean isBroken;

    private Job job;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime published;

    @OneToMany
    private List<Test> errors;

    public Build() {

    }

    public Build(String jobName, Long id) {
        this.job = new Job(jobName);
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDateTime getPublished() {
        return published;
    }

    public void setPublished(LocalDateTime published) {
        this.published = published;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(String jobName) {
        this.job = new Job(jobName);
    }

    public Long getTestJobId() {
        return testJobId;
    }

    public void setTestJobId(Long testJobId) {
        this.testJobId = testJobId;
    }

    public List<Test> getErrors() {
        return errors;
    }

    public void setErrors(List<Test> errors) {
        this.errors = errors;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void setIsBroken(boolean broken) {
        isBroken = broken;
    }

    @Override
    public String toString() {
        return "#" + id;
    }
}
