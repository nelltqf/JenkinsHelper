package happy.rabbit.analyzer;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.FailureReason;
import happy.rabbit.domain.TestResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MockAnalyzer implements Analyzer {

    @Override
    public String getFailureReason(Build build) {
        String failureReason = FailureReason.UNKNOWN.name();
        List<TestResult> failedTests = build.getTestResults()
                .stream()
                .filter(testResult -> !testResult.getStatus().isPassed())
                .collect(Collectors.toList());
        if (failedTests.isEmpty()) {
            return FailureReason.NONE.name();
        }
        return failureReason;
    }

    @Override
    public String getDescription(Build build) {
        String description = "";
        List<TestResult> failedTests = build.getTestResults()
                .stream()
                .filter(testResult -> !testResult.getStatus().isPassed())
                .collect(Collectors.toList());
        if (failedTests.isEmpty()) {
            return description;
        }
        return failedTests.get(0).getErrorDetails();
    }
}
