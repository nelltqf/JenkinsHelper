package happy.rabbit.analyzer;

import happy.rabbit.domain.Build;
import org.springframework.stereotype.Service;

@Service
public class MockAnalyzer implements Analyzer {

    @Override
    public String getFailureReason(Build build) {
        return "FAIL";
    }

    @Override
    public String getDescription(Build build) {
        return "Mock description";
    }
}
