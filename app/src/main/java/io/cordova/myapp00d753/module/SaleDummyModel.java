package io.cordova.myapp00d753.module;

/* renamed from: io.cordova.myapp00d753.module.SaleDummyModel */
public class SaleDummyModel {

    /* renamed from: id */
    String f424id;
    String name;

    public SaleDummyModel(String name2, String id) {
        this.name = name2;
        this.f424id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getId() {
        return this.f424id;
    }

    public void setId(String id) {
        this.f424id = id;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaleDummyModel)) {
            return false;
        }
        return ((SaleDummyModel) obj).name.equals(this.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}
