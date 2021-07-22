package io.cordova.myapp00d753.module;

public class SupSalaryModule {
    String empId,empName,month,year,amount,salaryUrl,ctcGros;

    public SupSalaryModule(String empId, String empName, String month, String year, String amount, String salaryUrl) {
        this.empId = empId;
        this.empName = empName;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.salaryUrl = salaryUrl;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSalaryUrl() {
        return salaryUrl;
    }

    public void setSalaryUrl(String salaryUrl) {
        this.salaryUrl = salaryUrl;
    }

    public String getCtcGros() {
        return ctcGros;
    }

    public void setCtcGros(String ctcGros) {
        this.ctcGros = ctcGros;
    }
}
