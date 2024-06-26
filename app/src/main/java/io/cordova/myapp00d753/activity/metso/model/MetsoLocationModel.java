package io.cordova.myapp00d753.activity.metso.model;

public class MetsoLocationModel {
    public int siteid;
    public String siteName;

    public MetsoLocationModel(int siteid, String siteName) {
        this.siteid = siteid;
        this.siteName = siteName;
    }

    public int getSiteid() {
        return siteid;
    }

    public void setSiteid(int siteid) {
        this.siteid = siteid;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
