package io.cordova.myapp00d753.module;

/* renamed from: io.cordova.myapp00d753.module.TeamSaleModule */
public class TeamSaleModule {
    String branchId;
    String count;

    /* renamed from: id */
    String f425id;
    String name;
    String name1;

    public TeamSaleModule(String id, String name2, String count2) {
        this.f425id = id;
        this.name = name2;
        this.count = count2;
    }

    public String getId() {
        return this.f425id;
    }

    public void setId(String id) {
        this.f425id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count2) {
        this.count = count2;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public void setBranchId(String branchId2) {
        this.branchId = branchId2;
    }
}
