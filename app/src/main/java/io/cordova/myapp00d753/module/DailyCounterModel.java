package io.cordova.myapp00d753.module;

public class DailyCounterModel {

    String punchInDate,punchInTime,punchInRemark,punchInAddr,punchInImage,punchOutDate,punchOutTime,punchOutRemark,punchOutAddr,punchOutImage,counter;

    public DailyCounterModel(String punchInDate, String punchInTime, String punchInRemark, String punchInAddr, String punchInImage, String punchOutDate, String punchOutTime, String punchOutRemark, String punchOutAddr, String punchOutImage,String counter) {
        this.punchInDate = punchInDate;
        this.punchInTime = punchInTime;
        this.punchInRemark = punchInRemark;
        this.punchInAddr = punchInAddr;
        this.punchInImage = punchInImage;
        this.punchOutDate = punchOutDate;
        this.punchOutTime = punchOutTime;
        this.punchOutRemark = punchOutRemark;
        this.punchOutAddr = punchOutAddr;
        this.punchOutImage = punchOutImage;
        this.counter=counter;
    }

    public String getPunchInDate() {
        return punchInDate;
    }

    public void setPunchInDate(String punchInDate) {
        this.punchInDate = punchInDate;
    }

    public String getPunchInTime() {
        return punchInTime;
    }

    public void setPunchInTime(String punchInTime) {
        this.punchInTime = punchInTime;
    }

    public String getPunchInRemark() {
        return punchInRemark;
    }

    public void setPunchInRemark(String punchInRemark) {
        this.punchInRemark = punchInRemark;
    }

    public String getPunchInAddr() {
        return punchInAddr;
    }

    public void setPunchInAddr(String punchInAddr) {
        this.punchInAddr = punchInAddr;
    }

    public String getPunchInImage() {
        return punchInImage;
    }

    public void setPunchInImage(String punchInImage) {
        this.punchInImage = punchInImage;
    }

    public String getPunchOutDate() {
        return punchOutDate;
    }

    public void setPunchOutDate(String punchOutDate) {
        this.punchOutDate = punchOutDate;
    }

    public String getPunchOutTime() {
        return punchOutTime;
    }

    public void setPunchOutTime(String punchOutTime) {
        this.punchOutTime = punchOutTime;
    }

    public String getPunchOutRemark() {
        return punchOutRemark;
    }

    public void setPunchOutRemark(String punchOutRemark) {
        this.punchOutRemark = punchOutRemark;
    }

    public String getPunchOutAddr() {
        return punchOutAddr;
    }

    public void setPunchOutAddr(String punchOutAddr) {
        this.punchOutAddr = punchOutAddr;
    }

    public String getPunchOutImage() {
        return punchOutImage;
    }

    public void setPunchOutImage(String punchOutImage) {
        this.punchOutImage = punchOutImage;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
