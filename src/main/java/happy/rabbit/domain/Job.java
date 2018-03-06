package happy.rabbit.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity(name = "job")
@Table(name = "job")
public class Job {

    @Id
    private String jobName;

    private boolean isPipeline;
    private boolean isActive;

    @ManyToMany
    private List<Job> testJobs;

    public Job() {

    }

    public Job(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPipeline() {
        return isPipeline;
    }

    public void setPipeline(boolean pipeline) {
        isPipeline = pipeline;
    }

    public List<Job> getTestJobs() {
        return testJobs;
    }

    public void setTestJobs(List<Job> testJobs) {
        this.testJobs = testJobs;
    }
}
