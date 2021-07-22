package io.cordova.myapp00d753.utility;

import android.util.Patterns;
import java.util.regex.Pattern;

/* renamed from: io.cordova.myapp00d753.utility.ValidUtils */
public class ValidUtils {
    public static boolean isValidEmail(String email) {
        return Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches();
    }

    public static boolean isAlphanumeric(String str) {
        if (str.length() >= 8) {
            return str.matches("^.*(?=.{8,16})(?=.*\\d)(?=.*[a-zA-Z]).*$");
        }
        return false;
    }

    public static boolean isValidMobile(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isValidphn(String phone) {
        return Pattern.compile(" ^/+[0-9]{10,13}$").matcher(phone).matches();
    }
}
