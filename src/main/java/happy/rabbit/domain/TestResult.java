package happy.rabbit.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "TEST")
@Entity
public class TestResult {

    @EmbeddedId
    private TestId testId = new TestId();

    private String errorDetails;

    private String errorStackTrace;

    private Status status;

    public String getName() {
        return testId.testName;
    }

    public void setName(String testName) {
        this.testId.setTestName(testName);
    }

    public TestId getTestId() {
        return testId;
    }

    public void setTestId(TestId testId) {
        this.testId = testId;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorMessage) {
        this.errorDetails = errorMessage;
    }

    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    public void setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setBuild(Build build) {
        testId.setBuild(build);
    }

    @Embeddable
    public class TestId implements Serializable {

        @ManyToOne
        @Cascade(org.hibernate.annotations.CascadeType.ALL)
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
}
