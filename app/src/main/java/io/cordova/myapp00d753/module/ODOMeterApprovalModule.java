package io.cordova.myapp00d753.module;

public class ODOMeterApprovalModule {
    String date,odometereading,jrType,imageData;
    private boolean isSelected = false;
    int aid;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOdometereading() {
        return odometereading;
    }

    public void setOdometereading(String odometereading) {
        this.odometereading = odometereading;
    }

    public String getJrType() {
        return jrType;
    }

    public void setJrType(String jrType) {
        this.jrType = jrType;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }
}
