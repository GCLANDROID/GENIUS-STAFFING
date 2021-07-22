package io.cordova.myapp00d753.module;

import java.io.Serializable;

public class VisitingDailyLogLocationModel implements Serializable {
    String location,time,store;

    public VisitingDailyLogLocationModel(String location, String time, String store) {
        this.location = location;
        this.time = time;
        this.store = store;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
