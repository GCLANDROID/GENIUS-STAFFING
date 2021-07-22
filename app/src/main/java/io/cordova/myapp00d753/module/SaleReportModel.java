package io.cordova.myapp00d753.module;

public class SaleReportModel {
    String target,sold,quater,percentage,month,finYear,preYear,preTarget,preSold,prePercentage,growth;

    public SaleReportModel(String target, String sold, String quater, String percentage, String month, String finYear, String preYear, String preTarget, String preSold, String prePercentage, String growth) {
        this.target = target;
        this.sold = sold;
        this.quater = quater;
        this.percentage = percentage;
        this.month = month;
        this.finYear = finYear;
        this.preYear = preYear;
        this.preTarget = preTarget;
        this.preSold = preSold;
        this.prePercentage = prePercentage;
        this.growth = growth;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getQuater() {
        return quater;
    }

    public void setQuater(String quater) {
        this.quater = quater;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
    }

    public String getPreYear() {
        return preYear;
    }

    public void setPreYear(String preYear) {
        this.preYear = preYear;
    }

    public String getPreTarget() {
        return preTarget;
    }

    public void setPreTarget(String preTarget) {
        this.preTarget = preTarget;
    }

    public String getPreSold() {
        return preSold;
    }

    public void setPreSold(String preSold) {
        this.preSold = preSold;
    }

    public String getPrePercentage() {
        return prePercentage;
    }

    public void setPrePercentage(String prePercentage) {
        this.prePercentage = prePercentage;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }
}
