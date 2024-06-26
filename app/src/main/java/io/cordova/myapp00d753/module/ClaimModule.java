package io.cordova.myapp00d753.module;

public class ClaimModule {
    String claimDate,claimAmount,claimType,claimStatus,claimId,apprvAmount;
    String costCenter,wbsCode,siteName,supervisor;

    public ClaimModule(String claimDate, String claimAmount, String claimType, String claimStatus, String claimId, String ApproveAmount) {
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.claimType = claimType;
        this.claimStatus = claimStatus;
        this.claimId=claimId;
        this.apprvAmount=ApproveAmount;
    }

    public ClaimModule(String claimDate, String claimAmount, String claimType, String claimStatus, String claimId, String apprvAmount, String costCenter, String wbsCode, String siteName, String supervisor) {
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.claimType = claimType;
        this.claimStatus = claimStatus;
        this.claimId = claimId;
        this.apprvAmount = apprvAmount;
        this.costCenter = costCenter;
        this.wbsCode = wbsCode;
        this.siteName = siteName;
        this.supervisor = supervisor;
    }

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

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getWbsCode() {
        return wbsCode;
    }

    public void setWbsCode(String wbsCode) {
        this.wbsCode = wbsCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}
