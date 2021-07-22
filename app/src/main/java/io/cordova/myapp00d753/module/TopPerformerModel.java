package io.cordova.myapp00d753.module;

public class TopPerformerModel {
    String name,target,sold,achv,branch,zone;

    public TopPerformerModel(String name, String target, String sold, String achv) {
        this.name = name;
        this.target = target;
        this.sold = sold;
        this.achv = achv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAchv() {
        return achv;
    }

    public void setAchv(String achv) {
        this.achv = achv;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
