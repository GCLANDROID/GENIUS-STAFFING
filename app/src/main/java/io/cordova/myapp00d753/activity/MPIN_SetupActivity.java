package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.otpview.OTPListener;
import com.otpview.OTPTextView;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.ShowDialog;

public class MPIN_SetupActivity extends AppCompatActivity {
    private static final String TAG = "MPIN_SetupActivity";
    OTPTextView otpTextView,otpViewConfirm;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv0;
    LinearLayout llBackspace;
    String MPIN="",CONFIRM_MPIN="";
    TextView tvMpinText;
    ImageView imgMpin;
    LinearLayout llEnterNewMPIN,llConfirmMPIN;
    Button btnMPIN;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin_setup);

        initView();
        btnClick();
    }

    private void initView() {
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
                Log.e(TAG, "onInteractionListener: "+otpTextView.getOtp());
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
                if(!MPIN.isEmpty() && MPIN.length() == 4){
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
                if(!MPIN.isEmpty() && !confirm_mpin.isEmpty()){
                    llConfirmMPIN.setBackgroundResource(0);
                }
            }
        });
    }

    private void btnClick() {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"1";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"1";
                    otpTextView.setOTP(otp);
                }

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"2";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"2";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"3";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"3";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"4";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"4";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"5";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"5";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"6";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"6";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"7";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"7";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"8";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"8";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"9";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"9";
                    otpTextView.setOTP(otp);
                }
            }
        });
        tv0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MPIN.isEmpty() && MPIN.length() == 4){
                    String otp=otpViewConfirm.getOtp()+"0";
                    otpViewConfirm.setOTP(otp);
                } else {
                    String otp=otpTextView.getOtp()+"0";
                    otpTextView.setOTP(otp);
                }
            }
        });
        llBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!otpViewConfirm.getOtp().isEmpty()){
                    llEnterNewMPIN.setBackgroundResource(0);
                    llConfirmMPIN.setBackgroundResource(R.drawable.border_background_2);
                    clearOTP(otpViewConfirm.getOtp(),otpViewConfirm);
                } else if (!otpTextView.getOtp().isEmpty()){
                    llEnterNewMPIN.setBackgroundResource(R.drawable.border_background_2);
                    llConfirmMPIN.setBackgroundResource(0);
                    clearOTP(otpTextView.getOtp(),otpTextView);
                }
                MPIN = otpTextView.getOtp();
                CONFIRM_MPIN = otpViewConfirm.getOtp();
            }
        });
        btnMPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: MPIN: "+MPIN+" CONFIRM_MPIN: "+CONFIRM_MPIN);
                if (MPIN.equals(CONFIRM_MPIN)){
                    ShowDialog.showSuccessDialog(MPIN_SetupActivity.this, "MPIN match successfully", new ShowDialog.ResultListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(MPIN_SetupActivity.this, NewLoginTwoActivity.class);
                            intent.putExtra("from","MPIN_setup");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                } else {
                    ShowDialog.showErrorDialog(MPIN_SetupActivity.this,"MPIN not match");
                }
            }
        });
    }

    private void clearOTP(String otp,OTPTextView clearOtpViewConfirm) {
        String part = otp.substring(0, otp.length()-1);
        Log.e(TAG, "clearOTP: "+part);
        clearOtpViewConfirm.setOTP(part);
    }
}
