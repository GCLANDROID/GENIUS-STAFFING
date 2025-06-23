package io.cordova.myapp00d753.module;

    public class ViewLeaveBalanceModel {
    String LeaveYear,LeaveTypeID,LeaveTypeName,Opening,Increment,Availed,Adjusted,Encasement,Closing;

    public ViewLeaveBalanceModel(String leaveYear, String leaveTypeID, String leaveTypeName, String opening, String increment, String availed, String adjusted, String encasement, String closing) {
        LeaveYear = leaveYear;
        LeaveTypeID = leaveTypeID;
        LeaveTypeName = leaveTypeName;
        Opening = opening;
        Increment = increment;
        Availed = availed;
        Adjusted = adjusted;
        Encasement = encasement;
        Closing = closing;
    }

    public String getLeaveYear() {
        return LeaveYear;
    }

    public void setLeaveYear(String leaveYear) {
        LeaveYear = leaveYear;
    }

    public String getLeaveTypeID() {
        return LeaveTypeID;
    }

    public void setLeaveTypeID(String leaveTypeID) {
        LeaveTypeID = leaveTypeID;
    }

    public String getLeaveTypeName() {
        return LeaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        LeaveTypeName = leaveTypeName;
    }

    public String getOpening() {
        return Opening;
    }

    public void setOpening(String opening) {
        Opening = opening;
    }

    public String getIncrement() {
        return Increment;
    }

    public void setIncrement(String increment) {
        Increment = increment;
    }

    public String getAvailed() {
        return Availed;
    }

    public void setAvailed(String availed) {
        Availed = availed;
    }

    public String getAdjusted() {
        return Adjusted;
    }

    public void setAdjusted(String adjusted) {
        Adjusted = adjusted;
    }

    public String getEncasement() {
        return Encasement;
    }

    public void setEncasement(String encasement) {
        Encasement = encasement;
    }

    public String getClosing() {
        return Closing;
    }

    public void setClosing(String closing) {
        Closing = closing;
    }
}
