package io.cordova.myapp00d753.utility;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class YearMonthUtil {
    public static ArrayList<String> getPreviousCurrentNextYearList() {
        ArrayList<String> yearList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        yearList.add(String.valueOf(currentYear - 1));
        yearList.add(String.valueOf(currentYear));
        yearList.add(String.valueOf(currentYear + 1));

        return yearList;
    }

    public static ArrayList<String> getMonthNumberList() {
        ArrayList<String> monthList = new ArrayList<>();
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < 12; i++) {   // only 12 months
            monthList.add(months[i]);
        }
        return monthList;
    }
}
