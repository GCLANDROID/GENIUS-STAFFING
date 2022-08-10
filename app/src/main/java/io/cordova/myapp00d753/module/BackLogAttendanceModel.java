package io.cordova.myapp00d753.module;

public class BackLogAttendanceModel {
    String date;
    String inTime;
    String outTime;
    String remarks;
    boolean selected=false;
    String dayType;



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

    public BackLogAttendanceModel(String date, String inTime, String outTime)
{
   this.date=date;
   this.inTime=inTime;
   this.outTime=outTime;



}

}
