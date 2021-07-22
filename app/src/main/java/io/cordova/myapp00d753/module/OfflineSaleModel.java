package io.cordova.myapp00d753.module;

public class OfflineSaleModel {
    String Address,date;
    int status;

    public OfflineSaleModel(String address, String date, int status) {
        Address = address;
        this.date = date;
        this.status = status;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
