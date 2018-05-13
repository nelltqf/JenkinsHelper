package happy.rabbit.analyzer;

import happy.rabbit.domain.Build;

public interface Analyzer {

    String getFailureReason(Build build);

    String getDescription(Build build);
}
