package happy.rabbit.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Test {

    @Id
    private String name;

    private String errorDetails;

    private String errorStackTrace;

    private Status status;

    public String getName() {
        return name;
    }

    public void setName(String testName) {
        this.name = testName;
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
}
