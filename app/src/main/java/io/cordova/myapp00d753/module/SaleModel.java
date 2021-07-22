package io.cordova.myapp00d753.module;

public class SaleModel {
    String date,category,model,name,email,phn,toke,ticket,appStatus,appRemarks;

    public SaleModel(String date, String category, String model, String name, String email, String phn, String toke, String ticket) {
        this.date = date;
        this.category = category;
        this.model = model;
        this.name = name;
        this.email = email;
        this.phn = phn;
        this.toke = toke;
        this.ticket = ticket;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getToke() {
        return toke;
    }

    public void setToke(String toke) {
        this.toke = toke;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getAppRemarks() {
        return appRemarks;
    }

    public void setAppRemarks(String appRemarks) {
        this.appRemarks = appRemarks;
    }
}
