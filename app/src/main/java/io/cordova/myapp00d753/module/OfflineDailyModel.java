package io.cordova.myapp00d753.module;

public class OfflineDailyModel {
    String date,inTime,outTime,inLocation,outLocation,inRemarks,outRemarks;

    public OfflineDailyModel(String date, String inTime, String outTime, String inLocation, String outLocation, String inRemarks, String outRemarks) {
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;
        this.inLocation = inLocation;
        this.outLocation = outLocation;
        this.inRemarks = inRemarks;
        this.outRemarks = outRemarks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getInLocation() {
        return inLocation;
    }

    public void setInLocation(String inLocation) {
        this.inLocation = inLocation;
    }

    public String getOutLocation() {
        return outLocation;
    }

    public void setOutLocation(String outLocation) {
        this.outLocation = outLocation;
    }

    public String getInRemarks() {
        return inRemarks;
    }

    public void setInRemarks(String inRemarks) {
        this.inRemarks = inRemarks;
    }

    public String getOutRemarks() {
        return outRemarks;
    }

    public void setOutRemarks(String outRemarks) {
        this.outRemarks = outRemarks;
    }
}
