package io.cordova.myapp00d753.module;

public class ProfileModule {
    private boolean isSelected = false;
    String empId,empCode,empName,DOJ,department,designation,location,gender,DOB,gurdianName,relationship,qualification,martialStatus,bloodGroup,perAddress,preAddress,phoneNumber,mobileNumber,emailId,pfNumber,esiNumber,bankName,accNumber,aadharNumber,uanNumber;

    public ProfileModule(String empId, String empCode, String empName, String DOJ, String department, String designation, String location, String gender, String DOB, String gurdianName, String relationship, String qualification, String martialStatus, String bloodGroup, String perAddress, String preAddress, String phoneNumber, String mobileNumber, String emailId, String pfNumber, String esiNumber, String bankName, String accNumber, String aadharNumber, String uanNumber) {
        this.empId = empId;
        this.empCode = empCode;
        this.empName = empName;
        this.DOJ = DOJ;
        this.department = department;
        this.designation = designation;
        this.location = location;
        this.gender = gender;
        this.DOB = DOB;
        this.gurdianName = gurdianName;
        this.relationship = relationship;
        this.qualification = qualification;
        this.martialStatus = martialStatus;
        this.bloodGroup = bloodGroup;
        this.perAddress = perAddress;
        this.preAddress = preAddress;
        this.phoneNumber = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.emailId = emailId;
        this.pfNumber = pfNumber;
        this.esiNumber = esiNumber;
        this.bankName = bankName;
        this.accNumber = accNumber;
        this.aadharNumber = aadharNumber;
        this.uanNumber = uanNumber;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDOJ() {
        return DOJ;
    }

    public void setDOJ(String DOJ) {
        this.DOJ = DOJ;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGurdianName() {
        return gurdianName;
    }

    public void setGurdianName(String gurdianName) {
        this.gurdianName = gurdianName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPerAddress() {
        return perAddress;
    }

    public void setPerAddress(String perAddress) {
        this.perAddress = perAddress;
    }

    public String getPreAddress() {
        return preAddress;
    }

    public void setPreAddress(String preAddress) {
        this.preAddress = preAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPfNumber() {
        return pfNumber;
    }

    public void setPfNumber(String pfNumber) {
        this.pfNumber = pfNumber;
    }

    public String getEsiNumber() {
        return esiNumber;
    }

    public void setEsiNumber(String esiNumber) {
        this.esiNumber = esiNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getUanNumber() {
        return uanNumber;
    }

    public void setUanNumber(String uanNumber) {
        this.uanNumber = uanNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
