package io.cordova.myapp00d753.module;

/* renamed from: io.cordova.myapp00d753.module.AttendanceModule */
public class AttendanceModule {
    String attendanceDate;
    String attendanceInTime;
    String attendanceLocation;
    String attendanceOutTime;
    String attendanceReply;
    String attendanceType;

    public AttendanceModule(String attendanceDate2, String attendanceInTime2, String attendanceOutTime2, String attendanceLocation2, String attendanceReply2, String attendanceType2) {
        this.attendanceDate = attendanceDate2;
        this.attendanceInTime = attendanceInTime2;
        this.attendanceOutTime = attendanceOutTime2;
        this.attendanceLocation = attendanceLocation2;
        this.attendanceReply = attendanceReply2;
        this.attendanceType = attendanceType2;
    }

    public String getAttendanceDate() {
        return this.attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate2) {
        this.attendanceDate = attendanceDate2;
    }

    public String getAttendanceInTime() {
        return this.attendanceInTime;
    }

    public void setAttendanceInTime(String attendanceInTime2) {
        this.attendanceInTime = attendanceInTime2;
    }

    public String getAttendanceOutTime() {
        return this.attendanceOutTime;
    }

    public void setAttendanceOutTime(String attendanceOutTime2) {
        this.attendanceOutTime = attendanceOutTime2;
    }

    public String getAttendanceLocation() {
        return this.attendanceLocation;
    }

    public void setAttendanceLocation(String attendanceLocation2) {
        this.attendanceLocation = attendanceLocation2;
    }

    public String getAttendanceReply() {
        return this.attendanceReply;
    }

    public void setAttendanceReply(String attendanceReply2) {
        this.attendanceReply = attendanceReply2;
    }

    public String getAttendanceType() {
        return this.attendanceType;
    }

    public void setAttendanceType(String attendanceType2) {
        this.attendanceType = attendanceType2;
    }
}
