package io.cordova.myapp00d753.module;

public class HolidayModel {
    String Date;
    String Holiday;

    public HolidayModel(String date, String holiday) {
        Date = date;
        Holiday = holiday;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getHoliday() {
        return Holiday;
    }

    public void setHoliday(String holiday) {
        Holiday = holiday;
    }
}
