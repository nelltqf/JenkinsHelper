package happy.rabbit.analyzer;

import happy.rabbit.domain.Build;
import happy.rabbit.domain.FailureReason;
import happy.rabbit.domain.TestResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SimpleAnalyzer implements Analyzer {

    @Override
    public String getFailureReason(Build build) {
        String failureReason = FailureReason.UNKNOWN.name();
        if (build.getFailedTests().isEmpty()) {
            return FailureReason.NONE.name();
        }
        return failureReason;
    }

    @Override
    public String getDescription(Build build) {
        String description = "";
        List<TestResult> failedTests = build.getFailedTests();
        if (failedTests.isEmpty()) {
            return description;
        }
        return failedTests.stream().map(TestResult::getErrorDetails).collect(Collectors.joining("\n"));
    }
}
