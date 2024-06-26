package io.cordova.myapp00d753.activity.metso.model;

public class MetsoShiftModel {
    public long workingShiftID;
    public String column1;

    public MetsoShiftModel(long workingShiftID, String column1) {
        this.workingShiftID = workingShiftID;
        this.column1 = column1;
    }

    public Object getWorkingShiftID() {
        return workingShiftID;
    }

    public void setWorkingShiftID(long workingShiftID) {
        this.workingShiftID = workingShiftID;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }
}
