package io.cordova.myapp00d753.module;

public class MulFenceConfigModel {
    String craetdOn,address, fencePoint;

    public MulFenceConfigModel(String craetdOn, String address, String gencePoint) {
        this.craetdOn = craetdOn;
        this.address = address;
        this.fencePoint = gencePoint;
    }

    public String getCraetdOn() {
        return craetdOn;
    }

    public void setCraetdOn(String craetdOn) {
        this.craetdOn = craetdOn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFencePoint() {
        return fencePoint;
    }

    public void setFencePoint(String fencePoint) {
        this.fencePoint = fencePoint;
    }
}
