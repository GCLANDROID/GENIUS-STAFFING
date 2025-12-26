package io.cordova.myapp00d753.activity.NEW.model;

public class AllApplicationViewModel {
    public String appliedType;
    public String applicationDate;
    public String appliedDate;
    public String intime;
    public String outtime;
    public String reason;
    public String refDate;
    public String selectedWorkPlace;
    public String selectedWorkingShift;
    public String selectedApprover;
    public String currentApprovalStatus;
    public String approvalDetails;
    public String finalApprovalStatus;

    public AllApplicationViewModel(String appliedType, String applicationDate, String appliedDate, String intime, String outtime, String reason, String refDate, String selectedWorkPlace, String selectedWorkingShift, String selectedApprover, String currentApprovalStatus, String approvalDetails, String finalApprovalStatus) {
        this.appliedType = appliedType;
        this.applicationDate = applicationDate;
        this.appliedDate = appliedDate;
        this.intime = intime;
        this.outtime = outtime;
        this.reason = reason;
        this.refDate = refDate;
        this.selectedWorkPlace = selectedWorkPlace;
        this.selectedWorkingShift = selectedWorkingShift;
        this.selectedApprover = selectedApprover;
        this.currentApprovalStatus = currentApprovalStatus;
        this.approvalDetails = approvalDetails;
        this.finalApprovalStatus = finalApprovalStatus;
    }

    public String getAppliedType() {
        return appliedType;
    }

    public void setAppliedType(String appliedType) {
        this.appliedType = appliedType;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getSelectedWorkPlace() {
        return selectedWorkPlace;
    }

    public void setSelectedWorkPlace(String selectedWorkPlace) {
        this.selectedWorkPlace = selectedWorkPlace;
    }

    public String getSelectedWorkingShift() {
        return selectedWorkingShift;
    }

    public void setSelectedWorkingShift(String selectedWorkingShift) {
        this.selectedWorkingShift = selectedWorkingShift;
    }

    public String getSelectedApprover() {
        return selectedApprover;
    }

    public void setSelectedApprover(String selectedApprover) {
        this.selectedApprover = selectedApprover;
    }

    public String getCurrentApprovalStatus() {
        return currentApprovalStatus;
    }

    public void setCurrentApprovalStatus(String currentApprovalStatus) {
        this.currentApprovalStatus = currentApprovalStatus;
    }

    public String getApprovalDetails() {
        return approvalDetails;
    }

    public void setApprovalDetails(String approvalDetails) {
        this.approvalDetails = approvalDetails;
    }

    public String getFinalApprovalStatus() {
        return finalApprovalStatus;
    }

    public void setFinalApprovalStatus(String finalApprovalStatus) {
        this.finalApprovalStatus = finalApprovalStatus;
    }
}
