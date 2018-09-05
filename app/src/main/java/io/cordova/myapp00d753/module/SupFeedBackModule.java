package io.cordova.myapp00d753.module;

public class SupFeedBackModule {
    String empId,empName,date,issueName,quey;
    String feedBackid;
    private boolean isSelected = false;
    public SupFeedBackModule(String empId, String empName, String date, String issueName, String quey,String feedBackId) {
        this.empId = empId;
        this.empName = empName;
        this.date = date;
        this.issueName = issueName;
        this.quey = quey;
        this.feedBackid=feedBackId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getQuey() {
        return quey;
    }

    public void setQuey(String quey) {
        this.quey = quey;
    }

    public String getFeedBackid() {
        return feedBackid;
    }

    public void setFeedBackid(String feedBackid) {
        this.feedBackid = feedBackid;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
