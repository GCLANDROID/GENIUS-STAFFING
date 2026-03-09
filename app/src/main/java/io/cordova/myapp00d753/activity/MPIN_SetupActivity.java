package io.cordova.myapp00d753.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.otpview.OTPListener;
import com.otpview.OTPTextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.NEW.NEW_HolidayMarkingActivity;
import io.cordova.myapp00d753.activity.attendance.AttenDanceDashboardActivity;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.ShowDialog;
import io.cordova.myapp00d753.utility.Util;

public class MPIN_SetupActivity extends AppCompatActivity {
    private static final String TAG = "MPIN_SetupActivity";
    OTPTextView otpTextView, otpViewConfirm;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv0;
    LinearLayout llBackspace;
    String MPIN = "", CONFIRM_MPIN = "";
    TextView tvMpinText;
    ImageView imgMpin;
    LinearLayout llEnterNewMPIN, llConfirmMPIN;
    Button btnMPIN;
    String MasterID;
    String androidID, security_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin_setup);

        initView();
        btnClick();
    }

    private void initView() {
        MasterID = getIntent().getStringExtra("MasterID");
        imgMpin = findViewById(R.id.imgMpin);
        Glide.with(this)
                .asGif()
                .load(R.drawable.finger_tap_keypad)
                .into(imgMpin);
        otpTextView = findViewById(R.id.otp_view);
        otpViewConfirm = findViewById(R.id.otp_view_confirm);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);
        tv9 = findViewById(R.id.tv9);
        tv0 = findViewById(R.id.tv0);
        tvMpinText = findViewById(R.id.tvMpinText);
        llBackspace = findViewById(R.id.llBackspace);
        llEnterNewMPIN = findViewById(R.id.llEnterNewMPIN);
        llConfirmMPIN = findViewById(R.id.llConfirmMPIN);
        btnMPIN = findViewById(R.id.btnMPIN);
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                Log.e(TAG, "onInteractionListener: " + otpTextView.getOtp());
            }

            @Override
            public void onOTPComplete(@NonNull String complete_otp) {
                //Toast.makeText(MPIN_SetupActivity.this, complete_otp, Toast.LENGTH_SHORT).show();
                /*if (MPIN.isEmpty()){
                    MPIN =  complete_otp;
                    if (MPIN.length() == 4){
                        otpTextView.setOTP("");
                        tvMpinText.setText("Confirm your MPIN");
                    }
                } else {
                    confirm_mpin = complete_otp;
                }
                Log.e(TAG, "onOTPComplete: MPIN:"+MPIN+" confirm_mpin: "+confirm_mpin );
                if(!MPIN.isEmpty() && !confirm_mpin.isEmpty()){
                    if (!MPIN.equals(confirm_mpin)){
                        otpTextView.showError();
                    } else {
                        otpTextView.showError();
                        Toast.makeText(MPIN_SetupActivity.this, "Your m-pin has been setup successfully", Toast.LENGTH_SHORT).show();
                    }
                }*/
                MPIN = complete_otp;
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    llEnterNewMPIN.setBackgroundResource(0);
                    llConfirmMPIN.setBackgroundResource(R.drawable.border_background_2);
                }
            }
        });

        otpViewConfirm.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(@NonNull String confirm_mpin) {
                CONFIRM_MPIN = confirm_mpin;
                if (!MPIN.isEmpty() && !confirm_mpin.isEmpty()) {
                    llConfirmMPIN.setBackgroundResource(0);
                }
            }
        });
        androidID = getAndroidID(MPIN_SetupActivity.this);
        if (androidID.equals("0")) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            androidID = telephonyManager.getDeviceId();
        } else {
            androidID = getAndroidID(MPIN_SetupActivity.this);
        }
    }

    private void btnClick() {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "1";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "1";
                    otpTextView.setOTP(otp);
                }

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "2";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "2";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "3";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "3";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "4";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "4";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "5";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "5";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "6";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "6";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "7";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "7";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "8";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "8";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "9";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "9";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4) {
                    String otp = otpViewConfirm.getOtp() + "0";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp = otpTextView.getOtp() + "0";
                    otpTextView.setOTP(otp);
                }
            }
        });
        llBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!otpViewConfirm.getOtp().isEmpty()) {
                    llEnterNewMPIN.setBackgroundResource(0);
                    llConfirmMPIN.setBackgroundResource(R.drawable.border_background_2);
                    clearOTP(otpViewConfirm.getOtp(), otpViewConfirm);
                } else if (!otpTextView.getOtp().isEmpty()) {
                    llEnterNewMPIN.setBackgroundResource(R.drawable.border_background_2);
                    llConfirmMPIN.setBackgroundResource(0);
                    clearOTP(otpTextView.getOtp(), otpTextView);
                }
                MPIN = otpTextView.getOtp();
                CONFIRM_MPIN = otpViewConfirm.getOtp();
            }
        });
        btnMPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: MPIN: " + MPIN + " CONFIRM_MPIN: " + CONFIRM_MPIN);
                if (MPIN.isEmpty() || MPIN.length() < 4) {
                    ShowDialog.showErrorDialog(MPIN_SetupActivity.this, "Please enter 4 digit MPIN");
                    return;
                }
                if (CONFIRM_MPIN.isEmpty() || CONFIRM_MPIN.length() < 4) {
                    ShowDialog.showErrorDialog(MPIN_SetupActivity.this, "Please enter 4 digit confirm MPIN");
                    return;
                }
                if (MPIN.equals(CONFIRM_MPIN)) {
                    if (MasterID.contains("AEM")) {
                        security_code = "0000";
                    } else if (MasterID.contains("FMS")) {
                        security_code = "222";
                    } else if (MasterID.contains("ITS")) {
                        security_code = "888";
                    } else if (MasterID.contains("SEC")) {
                        security_code = "333";
                    } else if (MasterID.contains("NAPS")) {
                        security_code = "444";
                    } else if (MasterID.contains("NPS")) {
                        security_code = "444";
                    } else if (MasterID.contains("GMSP")) {
                        security_code = "666";
                    } else if (MasterID.contains("MSP")) {
                        security_code = "666";
                    } else if (MasterID.contains("FSS")) {
                        security_code = "0000";
                    }


                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("MasterID", MasterID);
                        obj.put("mPin", MPIN);
                        obj.put("DeviceID", androidID);
                        obj.put("SecurityCode", security_code);
                        mpinSet(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShowDialog.showErrorDialog(MPIN_SetupActivity.this, "Confirm MPIN does not match with MPIN");
                }
            }
        });
    }

    private void clearOTP(String otp, OTPTextView clearOtpViewConfirm) {
        String part = otp.substring(0, otp.length() - 1);
        Log.e(TAG, "clearOTP: " + part);
        clearOtpViewConfirm.setOTP(part);
    }

    private String getAndroidID(Context context) {
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
    }

    private void mpinSet(JSONObject jsonObject) {
        Log.e("LOGIN", "login: " + jsonObject.toString());
        final ProgressDialog pd = new ProgressDialog(MPIN_SetupActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(AppData.newv2url + "Login/mPinSetReset")
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("LOGIN", "@@@@@@" + job1);
                        pd.dismiss();

                        String Response_Code = job1.optString("Response_Code");
                        String Response_Message = job1.optString("Response_Message");

                        if (Response_Code.equals("101")) {
                            ShowDialog.showSuccessDialog(MPIN_SetupActivity.this, Response_Message, new ShowDialog.ResultListener() {
                                @Override
                                public void onSuccess() {
                                    Intent intent = new Intent(MPIN_SetupActivity.this, NewLoginTwoActivity.class);
                                    intent.putExtra("from", "MPIN_setup");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });

                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();




                        } else {

                            ShowDialog.showErrorDialog(MPIN_SetupActivity.this, Response_Message);


                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("LOGIN", "onError: " + error);
                        pd.dismiss();


                    }
                });
    }
}
