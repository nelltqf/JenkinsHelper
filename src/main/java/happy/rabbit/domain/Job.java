package happy.rabbit.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "JOB")
@Entity
public class Job {

    private static final String PIPELINE = "WorkflowJob";

    @Id
    @Column(name = "ID")
    private String displayName;

    @Column(name = "IS_PIPELINE")
    private boolean isPipeline;

    @Column(name = "IS_ACTIVE")
    private boolean isActive = true;

    @OneToMany(mappedBy = "id.job")
    private List<Build> builds = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Job> testJobs = new ArrayList<>();

    public Job() {

    }

    public Job(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String jobName) {
        this.displayName = jobName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public boolean isPipeline() {
        return isPipeline;
    }

    public void setIsPipeline(boolean pipeline) {
        isPipeline = pipeline;
    }

    public List<Job> getTestJobs() {
        return testJobs;
    }

    public void setTestJobs(List<Job> testJobs) {
        this.testJobs = testJobs;
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public void setBuilds(List<Build> builds) {
        this.builds = builds;
    }

    public void set_class(String _class) {
        isPipeline = _class.endsWith(PIPELINE);
    }

    @Override
    public String toString() {
        return "Job{" +
                "displayName='" + displayName + '\'' +
                '}';
    }
}
