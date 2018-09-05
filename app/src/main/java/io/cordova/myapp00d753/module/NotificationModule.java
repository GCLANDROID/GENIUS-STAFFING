package io.cordova.myapp00d753.module;

public class NotificationModule {
    String tvTitle,tvBody,tvDate;

    public NotificationModule(String tvTitle, String tvBody, String tvDate) {
        this.tvTitle = tvTitle;
        this.tvBody = tvBody;
        this.tvDate = tvDate;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public String getTvBody() {
        return tvBody;
    }

    public void setTvBody(String tvBody) {
        this.tvBody = tvBody;
    }

    public String getTvDate() {
        return tvDate;
    }

    public void setTvDate(String tvDate) {
        this.tvDate = tvDate;
    }
}
