
package io.cordova.myapp00d753.activity.honnasa.model;


import com.google.gson.annotations.SerializedName;



public class StoreModel {
    public String $id;
    public Object SecurityCode;
    public Object ddltype;
    public String id;
    public Object id1;
    public Object id2;
    public Object id3;
    public String value;

    public StoreModel(String $id, Object securityCode, Object ddltype, String id, Object id1, Object id2, Object id3, String value) {
        this.$id = $id;
        SecurityCode = securityCode;
        this.ddltype = ddltype;
        this.id = id;
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
        this.value = value;
    }

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public Object getSecurityCode() {
        return SecurityCode;
    }

    public void setSecurityCode(Object securityCode) {
        SecurityCode = securityCode;
    }

    public Object getDdltype() {
        return ddltype;
    }

    public void setDdltype(Object ddltype) {
        this.ddltype = ddltype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getId1() {
        return id1;
    }

    public void setId1(Object id1) {
        this.id1 = id1;
    }

    public Object getId2() {
        return id2;
    }

    public void setId2(Object id2) {
        this.id2 = id2;
    }

    public Object getId3() {
        return id3;
    }

    public void setId3(Object id3) {
        this.id3 = id3;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
