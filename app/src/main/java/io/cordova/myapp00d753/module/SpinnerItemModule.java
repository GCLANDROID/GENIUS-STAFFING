package io.cordova.myapp00d753.module;

public class SpinnerItemModule {
    String item,itemId;

    public SpinnerItemModule(String item, String itemId) {
        this.item = item;
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
