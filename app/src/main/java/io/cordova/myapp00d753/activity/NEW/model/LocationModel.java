package io.cordova.myapp00d753.activity.NEW.model;

public class LocationModel {
    public String siteid;
    public String siteName;

    public LocationModel(String siteid, String siteName) {
        this.siteid = siteid;
        this.siteName = siteName;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
