package io.cordova.myapp00d753.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCalculation {
    private static final String TAG = "DateCalculation";
    public static boolean InputDateBeforeOrAfter(String Date) throws ParseException {
        //String inputDateStr = "01 Jan 2025";  // Change this to test with different dates
        Log.e(TAG, "InputDateBeforeOrAfter: "+Date);
        // Define the date format for input (dd MMM yyyy)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        boolean status = false;
        try {
            // Parse the input date
            Date inputDate = dateFormat.parse(Date);

            // Get the current date
            Date currentDate = new Date();

            // Compare the dates

            status = conditionCheck(inputDate,currentDate);
            return  status;
        } catch (Exception e) {
            Log.e(TAG, "Invalid date format.");
            if (Date.contains("Sep")){
                Date = Date.replace("Sep","Sept");
                Date inputDate = dateFormat.parse(Date);
                // Get the current date
                Date currentDate = new Date();
                status =  conditionCheck(inputDate,currentDate);
                Log.e(TAG, "Date correct.");
            }


            return status;
        }
    }

    private static boolean conditionCheck(Date inputDate, Date currentDate) {
        if (inputDate.before(currentDate)) {
            Log.e(TAG, "The input date is before the current date.");
            return true;
        } else if (inputDate.after(currentDate)) {
            Log.e(TAG, "The input date is after the current date.");
            return false;
        } else {
            Log.e(TAG, "The input date is the same as the current date.");
            return false;
        }
    }
}
