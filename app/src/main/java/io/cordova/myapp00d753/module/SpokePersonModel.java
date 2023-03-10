package io.cordova.myapp00d753.module;

public class SpokePersonModel {
    String personName,mobileNumber,email,branch;

    public SpokePersonModel(String personName, String mobileNumber, String email, String branch) {
        this.personName = personName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.branch = branch;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
