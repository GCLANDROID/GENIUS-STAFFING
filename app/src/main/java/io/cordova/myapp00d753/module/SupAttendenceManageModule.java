package io.cordova.myapp00d753.module;

public class SupAttendenceManageModule {
    String emoId,empName,empLocation,date;
    private boolean isSelected = false;

    public SupAttendenceManageModule(String emoId, String empName, String empLocation, String date) {
        this.emoId = emoId;
        this.empName = empName;
        this.empLocation = empLocation;
        this.date = date;
    }

    public String getEmoId() {
        return emoId;
    }

    public void setEmoId(String emoId) {
        this.emoId = emoId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpLocation() {
        return empLocation;
    }

    public void setEmpLocation(String empLocation) {
        this.empLocation = empLocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
