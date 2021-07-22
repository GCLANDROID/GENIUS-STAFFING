package io.cordova.myapp00d753.module;

public class ClaimDeleteModule {
    String claimDate,claimAmount,claimType,claimStatus,claimId;
    private boolean isSelected = false;

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(String claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public ClaimDeleteModule(String claimDate, String claimAmount, String claimType, String claimStatus, String claimId) {
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.claimType = claimType;
        this.claimStatus = claimStatus;
        this.claimId=claimId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
