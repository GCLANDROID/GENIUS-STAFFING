package io.cordova.myapp00d753.activity.honnasa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportModel {

    public String AEMEmployeeID;
    public int AEMStoreID;

    public int AEMProductID;

    public String Name;
    public int instock;
    public int closingstock;

    public String ReceiveDate;

    public String FinancialYear;

    public String Month;

    public int ReceiveStock;

    public int SalesStock;

    public double StockValue;

    public int TransID;

    public ReportModel(String AEMEmployeeID, int AEMStoreID, int AEMProductID, String name, int instock, int closingstock, String receiveDate, String financialYear, String month, int receiveStock, int salesStock, double stockValue, int transID) {
        this.AEMEmployeeID = AEMEmployeeID;
        this.AEMStoreID = AEMStoreID;
        this.AEMProductID = AEMProductID;
        Name = name;
        this.instock = instock;
        this.closingstock = closingstock;
        ReceiveDate = receiveDate;
        FinancialYear = financialYear;
        Month = month;
        ReceiveStock = receiveStock;
        SalesStock = salesStock;
        StockValue = stockValue;
        TransID = transID;
    }

    public String getAEMEmployeeID() {
        return AEMEmployeeID;
    }

    public void setAEMEmployeeID(String AEMEmployeeID) {
        this.AEMEmployeeID = AEMEmployeeID;
    }

    public int getAEMStoreID() {
        return AEMStoreID;
    }

    public void setAEMStoreID(int AEMStoreID) {
        this.AEMStoreID = AEMStoreID;
    }

    public int getAEMProductID() {
        return AEMProductID;
    }

    public void setAEMProductID(int AEMProductID) {
        this.AEMProductID = AEMProductID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getInstock() {
        return instock;
    }

    public void setInstock(int instock) {
        this.instock = instock;
    }

    public int getClosingstock() {
        return closingstock;
    }

    public void setClosingstock(int closingstock) {
        this.closingstock = closingstock;
    }

    public String getReceiveDate() {
        return ReceiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        ReceiveDate = receiveDate;
    }

    public String getFinancialYear() {
        return FinancialYear;
    }

    public void setFinancialYear(String financialYear) {
        FinancialYear = financialYear;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public int getReceiveStock() {
        return ReceiveStock;
    }

    public void setReceiveStock(int receiveStock) {
        ReceiveStock = receiveStock;
    }

    public int getSalesStock() {
        return SalesStock;
    }

    public void setSalesStock(int salesStock) {
        SalesStock = salesStock;
    }

    public double getStockValue() {
        return StockValue;
    }

    public void setStockValue(double stockValue) {
        StockValue = stockValue;
    }

    public int getTransID() {
        return TransID;
    }

    public void setTransID(int transID) {
        TransID = transID;
    }
}
