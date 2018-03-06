package happy.rabbit.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "build")
@Table(name = "build")
public class Build {

    @Id
    private Long id;

    private Long number;

    private Job job;

    private String descriptor;

    private boolean isBroken;

    private String failureReason;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime published;

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

    public LocalDateTime getPublished() {
        return published;
    }

    public void setPublished(LocalDateTime published) {
        this.published = published;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String content) {
        this.descriptor = content;
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

    public boolean isBroken() {
        return isBroken;
    }

    public void setIsBroken(boolean broken) {
        isBroken = broken;
    }

    @Override
    public String toString() {
        return "#" + number;
    }
}
