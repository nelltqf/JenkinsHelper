package happy.rabbit.domain;

import java.time.LocalDateTime;

public class JenkinsItem {

    private Integer id;
    private LocalDateTime published;
    private FailureReason failureReason;
    private String content;
    private boolean isBroken;

    public JenkinsItem() {

    }

    public JenkinsItem(int id, FailureReason failureReason, String content) {
        this.id = id;
        this.failureReason = failureReason;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getPublished() {
        return published;
    }

    public void setPublished(LocalDateTime published) {
        this.published = published;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void setIsBroken(boolean broken) {
        isBroken = broken;
    }

    @Override
    public String toString() {
        return "#" + id;
    }
}
