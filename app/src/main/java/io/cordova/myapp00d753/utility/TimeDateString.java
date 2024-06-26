package io.cordova.myapp00d753.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDateString {
    private static final String TAG = "TimeString";
    public static String inTime="",outTime="",indianPatternDate="";
    public interface TimeListener{
        void onSelect(String inTime,String outTime);
    }
    public static void getTimeFromString(String timeString){
        String firstString = "";
        String arrayString[] = timeString.split("\\(");
        Log.e(TAG, "getTimeFromString: "+arrayString[1]);
        String secondString = arrayString[1].replace(")","").trim();
        String arrayTime[] = secondString.split("-");
        Log.e(TAG, "inTime: "+arrayTime[0]+" outTime"+arrayTime[1]);
        inTime = arrayTime[0];
        outTime = arrayTime[1];
    }

    public static String changeDateStringToIndianDateFormat(String inputDate){
        String arrayString[] = inputDate.split("/");
        indianPatternDate = arrayString[1]+"/"+arrayString[0]+"/"+arrayString[2];
        return indianPatternDate;
    }

    public static String changeDateStringToIndianDateFormatAndMonthInSpellingSortFrom(String inputDateString){
        String indianPatternDateAndMonthInSpelling = "";
        String monthSpellingSortFrom = "";
        // Define input date format
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            // Parse the input string to a Date object
            Date inputDate = inputDateFormat.parse(inputDateString);
            // Define output date format
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yyyy");
            // Format the Date object to the desired output format
            String outputDateString = outputDateFormat.format(inputDate);
            // Print the result
            System.out.println("Original date: " + inputDateString);
            System.out.println("Converted date: " + outputDateString);
            indianPatternDateAndMonthInSpelling = outputDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return indianPatternDateAndMonthInSpelling;
    }
}
