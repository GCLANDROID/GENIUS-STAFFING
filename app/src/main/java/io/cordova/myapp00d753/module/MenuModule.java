package io.cordova.myapp00d753.module;

public class MenuModule {
    String menuName,menuDetails;
    boolean check;

    public MenuModule(String menuName, String menuDetails, boolean check) {
        this.menuName = menuName;
        this.menuDetails = menuDetails;
        this.check = check;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDetails() {
        return menuDetails;
    }

    public void setMenuDetails(String menuDetails) {
        this.menuDetails = menuDetails;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
