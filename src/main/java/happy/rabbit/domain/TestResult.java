package happy.rabbit.domain;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "TEST")
@Entity
public class TestResult {

    @EmbeddedId
    private TestId testId = new TestId();

    private String errorDetails;

    @Column(columnDefinition = "TEXT")
    private String errorStackTrace;

    private Status status;

    public String getName() {
        return testId.getTestName();
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

}
