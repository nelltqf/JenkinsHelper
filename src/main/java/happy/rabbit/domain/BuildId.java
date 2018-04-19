package happy.rabbit.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class BuildId implements Serializable {

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
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
