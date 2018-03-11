package happy.rabbit.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Job {

    private static final String PIPELINE = "WorkflowJob";

    @Id
    private String displayName;

    private boolean isPipeline;

    private boolean isActive = true;

    @OneToMany
    private List<Build> builds;

    @ManyToMany
    private List<Job> testJobs;

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

    public List<Build> getBuilds() {
        return builds;
    }

    public void setBuilds(List<Build> builds) {
        this.builds = builds;
    }

    public void set_class(String _class) {
        isPipeline = _class.endsWith(PIPELINE);
    }
}
