package io.cordova.myapp00d753.adapter;

public class NotificationModel {
    String Content, C_Url;

    public NotificationModel(String content, String c_Url) {
        Content = content;
        C_Url = c_Url;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getC_Url() {
        return C_Url;
    }

    public void setC_Url(String c_Url) {
        C_Url = c_Url;
    }
}
