package io.cordova.myapp00d753.module;

public class AddFaceModel {
    String url,time,faceId;

    public AddFaceModel(String url, String time, String faceId) {
        this.url = url;
        this.time = time;
        this.faceId = faceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }
}
