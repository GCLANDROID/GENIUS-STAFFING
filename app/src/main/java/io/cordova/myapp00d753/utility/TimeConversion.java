package io.cordova.myapp00d753.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeConversion {
    public static String convertAmPmTimeTo24HourFormat(String inputTime){
        String time24HourFormat="";
        try {
            // Define input and output date formats
            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            // Parse the input time string
            Date date = inputFormat.parse(inputTime);

            // Format the date in 24-hour format
            String outputTime = outputFormat.format(date);

            // Print the result
            System.out.println("Input Time: " + inputTime);
            System.out.println("Output Time (24-hour format): " + outputTime);
            time24HourFormat = outputTime;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time24HourFormat;
    }
}
