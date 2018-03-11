package happy.rabbit.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Test {

    @Id
    private String testName;

    private String errorMessage;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
