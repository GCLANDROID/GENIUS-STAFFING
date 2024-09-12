package io.cordova.myapp00d753.module;

public class HolidayMarkModel {
    String holiday;
    String holidayDate;

    public HolidayMarkModel(String holiday, String holidayDate) {
        this.holiday = holiday;
        this.holidayDate = holidayDate;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }
}
