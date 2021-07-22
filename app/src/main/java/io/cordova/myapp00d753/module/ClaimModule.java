package io.cordova.myapp00d753.module;

public class ClaimModule {
    String claimDate,claimAmount,claimType,claimStatus,claimId,apprvAmount;

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

    public String getApprvAmount() {
        return apprvAmount;
    }

    public void setApprvAmount(String apprvAmount) {
        this.apprvAmount = apprvAmount;
    }

    public ClaimModule(String claimDate, String claimAmount, String claimType, String claimStatus, String claimId, String ApproveAmount) {
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.claimType = claimType;
        this.claimStatus = claimStatus;
        this.claimId=claimId;
        this.apprvAmount=ApproveAmount;
    }
}
