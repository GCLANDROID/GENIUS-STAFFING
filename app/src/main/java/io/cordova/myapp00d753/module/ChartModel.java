package io.cordova.myapp00d753.module;

public class ChartModel {
    float item1,item4;
    int item2;
    String item3;
    double total;

    public ChartModel(float item1, int item2,String item3) {
        this.item1 = item1;
        this.item2 = item2;
       this.item3=item3;
    }

    public float getItem1() {
        return item1;
    }

    public void setItem1(float item1) {
        this.item1 = item1;
    }

    public int getItem2() {
        return item2;
    }

    public void setItem2(int item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public float getItem4() {
        return item4;
    }

    public void setItem4(float item4) {
        this.item4 = item4;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
