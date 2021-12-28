package io.cordova.myapp00d753.module;

public class MenuItemModel {
    String menuName;
    String menuId;

    public MenuItemModel(String menuName, String menuId) {
        this.menuName = menuName;
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
