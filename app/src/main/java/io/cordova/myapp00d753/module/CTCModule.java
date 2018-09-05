package io.cordova.myapp00d753.module;

public class CTCModule {
    String empId,empName,salary,ctcUrl;

    public CTCModule(String empId, String empName, String salary, String ctcUrl) {
        this.empId = empId;
        this.empName = empName;
        this.salary = salary;
        this.ctcUrl = ctcUrl;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCtcUrl() {
        return ctcUrl;
    }

    public void setCtcUrl(String ctcUrl) {
        this.ctcUrl = ctcUrl;
    }
}
