package io.cordova.myapp00d753.module;

public class LeaveBalanceDetailsModel {
    String leave,leaveBalance,leaveTaken,avaliable;

    public LeaveBalanceDetailsModel(String leave, String leaveBalance, String leaveTaken) {
        this.leave = leave;
        this.leaveBalance = leaveBalance;
        this.leaveTaken = leaveTaken;
    }

    public LeaveBalanceDetailsModel(String leave, String leaveBalance, String leaveTaken, String avaliable) {
        this.leave = leave;
        this.leaveBalance = leaveBalance;
        this.leaveTaken = leaveTaken;
        this.avaliable = avaliable;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(String leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public String getLeaveTaken() {
        return leaveTaken;
    }

    public void setLeaveTaken(String leaveTaken) {
        this.leaveTaken = leaveTaken;
    }

    public String getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(String avaliable) {
        this.avaliable = avaliable;
    }
}
