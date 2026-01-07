package io.cordova.myapp00d753.activity.NEW.model;

public class AttendanceReportModel {
        String slNo;
        String date;
        String intime;
        String outTime;
        String AttendanceStatus;
        String firstHalfStatus;
        String secondHalfStatus ;
        String workingHours;
        String inAddress;
        String inPunchSource;
        String outAddress;
        String outPunchSource;
        String fName;
        String inImage;
        String outImage;
        String location;
        String workingShift;
        String selectedApprover;
        String regularisation;
        String approvalStatus;
        String dayType;

        public AttendanceReportModel(String slNo,String date, String intime, String outTime, String attendanceStatus, String firstHalfStatus, String secondHalfStatus, String workingHours, String inAddress, String inPunchSource, String outAddress, String outPunchSource, String fName, String inImage, String outImage, String location, String workingShift, String selectedApprover, String regularisation, String approvalStatus,String dayType) {
                this.date = date;
                this.intime = intime;
                this.outTime = outTime;
                AttendanceStatus = attendanceStatus;
                this.firstHalfStatus = firstHalfStatus;
                this.secondHalfStatus = secondHalfStatus;
                this.workingHours = workingHours;
                this.inAddress = inAddress;
                this.inPunchSource = inPunchSource;
                this.outAddress = outAddress;
                this.outPunchSource = outPunchSource;
                this.fName = fName;
                this.inImage = inImage;
                this.outImage = outImage;
                this.location = location;
                this.workingShift = workingShift;
                this.selectedApprover = selectedApprover;
                this.regularisation = regularisation;
                this.approvalStatus = approvalStatus;
                this.dayType = dayType;
        }

        public String getSlNo() {
                return slNo;
        }

        public void setSlNo(String slNo) {
                this.slNo = slNo;
        }

        public String getDate() {
                return date;
        }

        public void setDate(String date) {
                this.date = date;
        }

        public String getIntime() {
                return intime;
        }

        public void setIntime(String intime) {
                this.intime = intime;
        }

        public String getOutTime() {
                return outTime;
        }

        public void setOutTime(String outTime) {
                this.outTime = outTime;
        }

        public String getAttendanceStatus() {
                return AttendanceStatus;
        }

        public void setAttendanceStatus(String attendanceStatus) {
                AttendanceStatus = attendanceStatus;
        }

        public String getFirstHalfStatus() {
                return firstHalfStatus;
        }

        public void setFirstHalfStatus(String firstHalfStatus) {
                this.firstHalfStatus = firstHalfStatus;
        }

        public String getSecondHalfStatus() {
                return secondHalfStatus;
        }

        public void setSecondHalfStatus(String secondHalfStatus) {
                this.secondHalfStatus = secondHalfStatus;
        }

        public String getWorkingHours() {
                return workingHours;
        }

        public void setWorkingHours(String workingHours) {
                this.workingHours = workingHours;
        }

        public String getInAddress() {
                return inAddress;
        }

        public void setInAddress(String inAddress) {
                this.inAddress = inAddress;
        }

        public String getInPunchSource() {
                return inPunchSource;
        }

        public void setInPunchSource(String inPunchSource) {
                this.inPunchSource = inPunchSource;
        }

        public String getOutAddress() {
                return outAddress;
        }

        public void setOutAddress(String outAddress) {
                this.outAddress = outAddress;
        }

        public String getOutPunchSource() {
                return outPunchSource;
        }

        public void setOutPunchSource(String outPunchSource) {
                this.outPunchSource = outPunchSource;
        }

        public String getfName() {
                return fName;
        }

        public void setfName(String fName) {
                this.fName = fName;
        }

        public String getInImage() {
                return inImage;
        }

        public void setInImage(String inImage) {
                this.inImage = inImage;
        }

        public String getOutImage() {
                return outImage;
        }

        public void setOutImage(String outImage) {
                this.outImage = outImage;
        }

        public String getLocation() {
                return location;
        }

        public void setLocation(String location) {
                this.location = location;
        }

        public String getWorkingShift() {
                return workingShift;
        }

        public void setWorkingShift(String workingShift) {
                this.workingShift = workingShift;
        }

        public String getSelectedApprover() {
                return selectedApprover;
        }

        public void setSelectedApprover(String selectedApprover) {
                this.selectedApprover = selectedApprover;
        }

        public String getRegularisation() {
                return regularisation;
        }

        public void setRegularisation(String regularisation) {
                this.regularisation = regularisation;
        }

        public String getApprovalStatus() {
                return approvalStatus;
        }

        public void setApprovalStatus(String approvalStatus) {
                this.approvalStatus = approvalStatus;
        }

        public String getDayType() {
                return dayType;
        }

        public void setDayType(String dayType) {
                this.dayType = dayType;
        }
}
