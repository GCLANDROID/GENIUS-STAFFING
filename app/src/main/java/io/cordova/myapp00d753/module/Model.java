package io.cordova.myapp00d753.module;

public class Model {
    String date,empname,tokenNo;

    public Model(String date, String empname, String tokenNo) {
        this.date = date;
        this.empname = empname;
        this.tokenNo = tokenNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }
}
