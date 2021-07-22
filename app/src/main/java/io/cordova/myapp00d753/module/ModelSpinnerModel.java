package io.cordova.myapp00d753.module;

public class ModelSpinnerModel {
    String value,id,mrp;

    public ModelSpinnerModel(String value, String id, String mrp) {
        this.value = value;
        this.id = id;
        this.mrp = mrp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
}
