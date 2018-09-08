package io.cordova.myapp00d753.module;

public class MenuModule {
    String menuName,menuDetails;
    public boolean isSelected = false;

    public MenuModule(String menuName, String menuDetails, boolean isSelected) {
        this.menuName = menuName;
        this.menuDetails = menuDetails;
        this.isSelected = isSelected;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
