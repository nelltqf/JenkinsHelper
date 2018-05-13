package happy.rabbit.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class BuildId implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "JOB_ID")
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof BuildId) {
            BuildId other = (BuildId) obj;
            return this.id.equals(other.id)
                    && this.job.getDisplayName().equals(other.job.getDisplayName());
        }

        return false;
    }
}
