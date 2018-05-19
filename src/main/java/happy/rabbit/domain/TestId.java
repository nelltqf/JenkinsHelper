package happy.rabbit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class TestId implements Serializable {

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Build build;

    private String testName;

    public BuildId getBuildId() {
        return build.getBuildId();
    }

    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
