package io.cordova.myapp00d753.activity.bosch.model;

public class BoschPunchTypeModel {
    public int punchtypeid;
    public String punchtypename;

    public BoschPunchTypeModel(int punchtypeid, String punchtypename) {
        this.punchtypeid = punchtypeid;
        this.punchtypename = punchtypename;
    }

    public int getPunchtypeid() {
        return punchtypeid;
    }

    public void setPunchtypeid(int punchtypeid) {
        this.punchtypeid = punchtypeid;
    }

    public String getPunchtypename() {
        return punchtypename;
    }

    public void setPunchtypename(String punchtypename) {
        this.punchtypename = punchtypename;
    }
}
