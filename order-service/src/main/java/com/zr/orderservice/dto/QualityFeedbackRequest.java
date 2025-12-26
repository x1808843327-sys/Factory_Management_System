package com.zr.orderservice.dto;

public class QualityFeedbackRequest {

    private String issueId;

    /**
     * PASS / FAIL
     */
    private String qualityResult;

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getQualityResult() {
        return qualityResult;
    }

    public void setQualityResult(String qualityResult) {
        this.qualityResult = qualityResult;
    }
}
