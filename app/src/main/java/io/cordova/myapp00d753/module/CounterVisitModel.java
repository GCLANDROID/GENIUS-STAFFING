package io.cordova.myapp00d753.module;

public class CounterVisitModel {
    String date,visitCount, month,preCount;

    public CounterVisitModel(String date, String visitCount) {
        this.date = date;
        this.visitCount = visitCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPreCount() {
        return preCount;
    }

    public void setPreCount(String preCount) {
        this.preCount = preCount;
    }
}
