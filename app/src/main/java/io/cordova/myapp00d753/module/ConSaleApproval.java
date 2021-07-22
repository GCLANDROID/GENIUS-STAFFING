package io.cordova.myapp00d753.module;

/* renamed from: io.cordova.myapp00d753.module.ConSaleApproval */
public class ConSaleApproval {
    String appSale;
    String branchId;
    String name;
    String pendingSale;
    String rejSale;
    String totalSale;
    String zoneId;

    public ConSaleApproval(String name2, String totalSale2, String pendingSale2, String appSale2, String rejSale2, String branchId2, String zoneId2) {
        this.name = name2;
        this.totalSale = totalSale2;
        this.pendingSale = pendingSale2;
        this.appSale = appSale2;
        this.rejSale = rejSale2;
        this.branchId = branchId2;
        this.zoneId = zoneId2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getTotalSale() {
        return this.totalSale;
    }

    public void setTotalSale(String totalSale2) {
        this.totalSale = totalSale2;
    }

    public String getPendingSale() {
        return this.pendingSale;
    }

    public void setPendingSale(String pendingSale2) {
        this.pendingSale = pendingSale2;
    }

    public String getAppSale() {
        return this.appSale;
    }

    public void setAppSale(String appSale2) {
        this.appSale = appSale2;
    }

    public String getRejSale() {
        return this.rejSale;
    }

    public void setRejSale(String rejSale2) {
        this.rejSale = rejSale2;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public void setBranchId(String branchId2) {
        this.branchId = branchId2;
    }

    public String getZoneId() {
        return this.zoneId;
    }

    public void setZoneId(String zoneId2) {
        this.zoneId = zoneId2;
    }
}
