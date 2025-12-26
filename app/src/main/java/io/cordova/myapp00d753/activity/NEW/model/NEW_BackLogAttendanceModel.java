package io.cordova.myapp00d753.activity.NEW.model;

public class NEW_BackLogAttendanceModel {
    String date;
    String inTime;
    String outTime;
    String remarks;
    boolean selected=false;
    String dayType;
    long shiftID=0;
    String remarks2="";
    String RemarksCode="";
    String SLNo="";

    String approver="";
    String approverID="0";
    String shift="";
    String location="";
    String locationID="0";





    public NEW_BackLogAttendanceModel(String date, String inTime, String outTime, boolean selected) {
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;
        this.selected = selected;
    }

    public NEW_BackLogAttendanceModel(String date, String inTime, String outTime) {
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public long getShiftID() {
        return shiftID;
    }

    public void setShiftID(long shiftID) {
        this.shiftID = shiftID;
    }


    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getRemarksCode() {
        return RemarksCode;
    }

    public void setRemarksCode(String remarksCode) {
        RemarksCode = remarksCode;
    }

    public String getSLNo() {
        return SLNo;
    }

    public void setSLNo(String SLNo) {
        this.SLNo = SLNo;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
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

    public String getApproverID() {
        return approverID;
    }

    public void setApproverID(String approverID) {
        this.approverID = approverID;
    }


    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }
}
