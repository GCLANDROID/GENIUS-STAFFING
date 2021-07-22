package io.cordova.myapp00d753.module;

public class SaleApprovalModule {
    private boolean isSelected = false;
    String date,empName,token,cusName,cateGory,branch,zone;

    public SaleApprovalModule(String date, String empName, String token, String cusName,String category) {
        this.date = date;
        this.empName = empName;
        this.token = token;
        this.cusName = cusName;
        this.cateGory=category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCateGory() {
        return cateGory;
    }

    public void setCateGory(String cateGory) {
        this.cateGory = cateGory;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
