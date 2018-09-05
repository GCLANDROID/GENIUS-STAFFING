package io.cordova.myapp00d753.module;

public class FeedBackModule {
    String feedbackDate,feedbackRemark,feedbackReply;

    public FeedBackModule(String feedbackDate,  String feedbackRemark, String feedbackReply) {
        this.feedbackDate = feedbackDate;

        this.feedbackRemark = feedbackRemark;
        this.feedbackReply = feedbackReply;
    }

    public String getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getFeedbackRemark() {
        return feedbackRemark;
    }

    public void setFeedbackRemark(String feedbackRemark) {
        this.feedbackRemark = feedbackRemark;
    }

    public String getFeedbackReply() {
        return feedbackReply;
    }

    public void setFeedbackReply(String feedbackReply) {
        this.feedbackReply = feedbackReply;
    }
}
