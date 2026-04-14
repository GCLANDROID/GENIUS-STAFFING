package io.cordova.myapp00d753.module;

public class CheckInOutAttendanceModel {
    String AEMEmployeeID,EmpName,InTime,Outtime;
    private boolean isSelected = false;


    public String getAEMEmployeeID() {
        return AEMEmployeeID;
    }

    public void setAEMEmployeeID(String AEMEmployeeID) {
        this.AEMEmployeeID = AEMEmployeeID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOuttime() {
        return Outtime;
    }

    public void setOuttime(String outtime) {
        Outtime = outtime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
