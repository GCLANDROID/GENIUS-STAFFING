package io.cordova.myapp00d753.module;

public class HonasaSalesReportModel {
    String AEMProductName,InStock,Sales,Return,RecStock,LastSalesDate;
    boolean isExapand;

    public String getAEMProductName() {
        return AEMProductName;
    }

    public void setAEMProductName(String AEMProductName) {
        this.AEMProductName = AEMProductName;
    }

    public String getInStock() {
        return InStock;
    }

    public void setInStock(String inStock) {
        InStock = inStock;
    }

    public String getSales() {
        return Sales;
    }

    public void setSales(String sales) {
        Sales = sales;
    }

    public String getReturn() {
        return Return;
    }

    public void setReturn(String aReturn) {
        Return = aReturn;
    }

    public String getRecStock() {
        return RecStock;
    }

    public void setRecStock(String recStock) {
        RecStock = recStock;
    }

    public boolean isExapand() {
        return isExapand;
    }

    public void setExapand(boolean exapand) {
        isExapand = exapand;
    }

    public String getLastSalesDate() {
        return LastSalesDate;
    }

    public void setLastSalesDate(String lastSalesDate) {
        LastSalesDate = lastSalesDate;
    }
}
