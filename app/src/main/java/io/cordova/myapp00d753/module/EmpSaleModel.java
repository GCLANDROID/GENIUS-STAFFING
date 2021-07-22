package io.cordova.myapp00d753.module;

public class EmpSaleModel {
    String empName,empId,percenTage,branch,zone;

    public EmpSaleModel(String empName, String empId,String percentage) {
        this.empName = empName;
        this.empId = empId;
        this.percenTage=percentage;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPercenTage() {
        return percenTage;
    }

    public void setPercenTage(String percenTage) {
        this.percenTage = percenTage;
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
