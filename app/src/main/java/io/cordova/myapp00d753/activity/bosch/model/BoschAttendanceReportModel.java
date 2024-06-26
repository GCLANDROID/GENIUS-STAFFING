package io.cordova.myapp00d753.activity.bosch.model;

import com.google.gson.annotations.SerializedName;

public class BoschAttendanceReportModel {
    String attendanceDate;
    String attendanceInTime;
    String attendanceOutTime;
    String attendanceLocation;

    String attendanceReply;
    String attendanceType;
    @SerializedName("Longitude")
    String shift;
    @SerializedName("Latitude")
    String location;
    String InMode;
    String OutMode;

    public BoschAttendanceReportModel(String attendanceDate, String attendanceInTime,  String attendanceOutTime, String attendanceLocation,String attendanceReply, String attendanceType, String shift, String location, String inMode, String outMode) {
        this.attendanceDate = attendanceDate;
        this.attendanceInTime = attendanceInTime;
        this.attendanceLocation = attendanceLocation;
        this.attendanceOutTime = attendanceOutTime;
        this.attendanceReply = attendanceReply;
        this.attendanceType = attendanceType;
        this.shift = shift;
        this.location = location;
        InMode = inMode;
        OutMode = outMode;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceInTime() {
        return attendanceInTime;
    }

    public void setAttendanceInTime(String attendanceInTime) {
        this.attendanceInTime = attendanceInTime;
    }

    public String getAttendanceLocation() {
        return attendanceLocation;
    }

    public void setAttendanceLocation(String attendanceLocation) {
        this.attendanceLocation = attendanceLocation;
    }

    public String getAttendanceOutTime() {
        return attendanceOutTime;
    }

    public void setAttendanceOutTime(String attendanceOutTime) {
        this.attendanceOutTime = attendanceOutTime;
    }

    public String getAttendanceReply() {
        return attendanceReply;
    }

    public void setAttendanceReply(String attendanceReply) {
        this.attendanceReply = attendanceReply;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInMode() {
        return InMode;
    }

    public void setInMode(String inMode) {
        InMode = inMode;
    }

    public String getOutMode() {
        return OutMode;
    }

    public void setOutMode(String outMode) {
        OutMode = outMode;
    }
}
