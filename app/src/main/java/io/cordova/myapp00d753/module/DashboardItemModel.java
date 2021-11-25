package io.cordova.myapp00d753.module;

public class DashboardItemModel {
    String itemName;
    int itempic;

    public DashboardItemModel(String itemName, int itempic) {
        this.itemName = itemName;
        this.itempic = itempic;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItempic() {
        return itempic;
    }

    public void setItempic(int itempic) {
        this.itempic = itempic;
    }
}
