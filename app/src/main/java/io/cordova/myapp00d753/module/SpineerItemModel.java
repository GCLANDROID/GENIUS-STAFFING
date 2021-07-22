package io.cordova.myapp00d753.module;

public class SpineerItemModel {
    String itemName,itemId;

    public SpineerItemModel(String itemName, String itemId) {
        this.itemName = itemName;
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
