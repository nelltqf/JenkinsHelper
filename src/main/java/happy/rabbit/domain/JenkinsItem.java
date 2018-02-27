package happy.rabbit.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "jenkins_item")
@Table(name = "jenkins_item")
public class JenkinsItem {

    @EmbeddedId
    private ItemJobId itemJobId;

    private FailureReason failureReason;

    private String content;

    private Long testJobId;

    private boolean isBroken;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime published;

    @OneToMany
    private List<Test> errors;

    public JenkinsItem() {

    }

    public ItemJobId getItemJobId() {
        return itemJobId;
    }

    public void setItemJobId(String jobName, Long id) {
        this.itemJobId = new ItemJobId(jobName, id);
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
        return this.itemJobId.job;
    }

    public void setJob(String jobName) {
        this.itemJobId.job = new Job(jobName);
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
        return "#" + itemJobId;
    }

    public void setJobName(String jobName) {
        this.itemJobId.job.setJobName(jobName);
    }

    @Embeddable
    public class ItemJobId implements Serializable {

        @Id
        private Long id;

        @Id
        @ManyToOne
        private Job job;

        public ItemJobId() {

        }

        public ItemJobId(String jobName, Long id) {
            this.id = id;
            this.job = new Job(jobName);
        }

        public String getJobName() {
            return this.job.getJobName();
        }

        public Long getId() {
            return this.id;
        }
    }
}
