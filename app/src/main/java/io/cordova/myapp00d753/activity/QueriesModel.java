package io.cordova.myapp00d753.activity;

public class QueriesModel {
    String date,issue,query,reply,status;

    public QueriesModel(String date, String issue, String query, String reply, String status) {
        this.date = date;
        this.issue = issue;
        this.query = query;
        this.reply = reply;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
