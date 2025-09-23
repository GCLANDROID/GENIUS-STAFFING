package io.cordova.myapp00d753.module;

public class SalaryModule {
    String year,month,amount,surl,Charges;
    int IsPaidService;

    public SalaryModule(String year, String month, String amount,String surl) {
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.surl=surl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getCharges() {
        return Charges;
    }

    public void setCharges(String charges) {
        Charges = charges;
    }

    public int getIsPaidService() {
        return IsPaidService;
    }

    public void setIsPaidService(int isPaidService) {
        IsPaidService = isPaidService;
    }
}
