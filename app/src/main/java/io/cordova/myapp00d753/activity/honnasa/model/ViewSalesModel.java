package io.cordova.myapp00d753.activity.honnasa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ViewSalesModel {
    @JsonProperty("OpenStock")
    public int openStock;
    @JsonProperty("ProductValue")
    public double productValue;
    @JsonProperty("InStock")
    public int inStock;
    @JsonProperty("AEMProductID")
    public int AEMProductID;
    @JsonProperty("PRODUCTNAME")
    public String productName;

    public String receivingStock;
    public String salesDone;
    public String salesValues;
    public String closingStock;

    public boolean isModified;



    public ViewSalesModel(int openStock, double productValue, int inStock, int AEMProductID, String productName) {
        this.openStock = openStock;
        this.productValue = productValue;
        this.inStock = inStock;
        this.AEMProductID = AEMProductID;
        this.productName = productName;
    }

    public int getOpenStock() {
        return openStock;
    }

    public void setOpenStock(int openStock) {
        this.openStock = openStock;
    }

    public double getProductValue() {
        return productValue;
    }

    public void setProductValue(double productValue) {
        this.productValue = productValue;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getAEMProductID() {
        return AEMProductID;
    }

    public void setAEMProductID(int AEMProductID) {
        this.AEMProductID = AEMProductID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReceivingStock() {
        return receivingStock;
    }

    public void setReceivingStock(String receivingStock) {
        this.receivingStock = receivingStock;
    }

    public String getSalesDone() {
        return salesDone;
    }

    public void setSalesDone(String salesDone) {
        this.salesDone = salesDone;
    }

    public String getSalesValues() {
        return salesValues;
    }

    public void setSalesValues(String salesValues) {
        this.salesValues = salesValues;
    }

    public String getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(String closingStock) {
        this.closingStock = closingStock;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }
}
