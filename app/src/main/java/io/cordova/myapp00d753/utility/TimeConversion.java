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

    public static String convert_HH_mm_To_HH_mm_ss(String inputTime){
        String HH_mm_ss_format = "" ;
        SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Define the new format
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        try {
            // Parse the original time
            Date date = originalFormat.parse(inputTime);
            // Reformat the parsed date
            HH_mm_ss_format =  newFormat.format(date);
            return HH_mm_ss_format;
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }

    }
}
