package io.cordova.myapp00d753.utility;

import android.app.AlertDialog;
import android.content.Context;

public class MaskingUtility {
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return phone;

        int visible = 4; // show last 4 digits
        int maskLength = phone.length() - visible;

        String masked = new String(new char[maskLength]).replace("\0", "*");
        return masked + phone.substring(phone.length() - visible);
    }

    // Aadhaar: XXXX-XXXX-1234
    public static String maskAadhaar(String aadhaar) {
        if (aadhaar == null || aadhaar.length() != 12) return aadhaar;

        return "XXXX-XXXX-" + aadhaar.substring(8);
    }

    // PAN: ABCDE1234F → A****1234F
    public static String maskPAN(String pan) {
        if (pan == null || pan.length() != 10) return pan;

        return pan.substring(0, 1) + "****" + pan.substring(5);
    }

    // UAN: 12-digit → ******789012
    public static String maskUAN(String uan) {
        if (uan == null || uan.length() < 6) return uan;

        int visible = 6;
        int maskLength = uan.length() - visible;

        return new String(new char[maskLength]).replace("\0", "*")
                + uan.substring(uan.length() - visible);
    }

    // Bank Account: *********4567 (show last 4)
    public static String maskBankAccount(String acc) {
        if (acc == null || acc.length() < 4) return acc;

        int visible = 4;
        int maskLength = acc.length() - visible;

        return new String(new char[maskLength]).replace("\0", "*")
                + acc.substring(acc.length() - visible);
    }

    // PF Number Example: KN/BN/1234567/000 — keep last 3 or last block
    public static String maskPFNumber(String pf) {
        if (pf == null || pf.length() < 4) return pf;

        // Mask all except last 4 characters
        int visible = 4;
        int maskLength = pf.length() - visible;

        return new String(new char[maskLength]).replace("\0", "*")
                + pf.substring(pf.length() - visible);
    }
    public static String maskESINumber(String esi) {
        if (esi == null || esi.length() < 4) return esi;

        int visible = 4; // show last 4 digits
        int maskLength = esi.length() - visible;

        return new String(new char[maskLength]).replace("\0", "*")
                + esi.substring(esi.length() - visible);
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;

        String[] parts = email.split("@");
        String user = parts[0];
        String domain = parts[1];

        if (user.length() <= 2) {
            // For short usernames like "ab@mail.com" → a*@mail.com
            return user.charAt(0) + "*" + "@" + domain;
        }

        String maskedUser = user.charAt(0) + "***" + user.charAt(user.length() - 1);

        return maskedUser + "@" + domain;
    }

    public static void showUnmaskedDialog(Context context, String unmasedValue, String title) {

        String message = unmasedValue;


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("" + title + " Details");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}

