package io.cordova.myapp00d753.activity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

public class MPIN_SetupActivity extends AppCompatActivity {
    private static final String TAG = "MPIN_SetupActivity";
    OTPTextView otpTextView;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv0;
    LinearLayout llBackspace;
    String MPIN="",confirm_mpin="";
    TextView tvMpinText;
    ImageView imgMpin;
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
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                Log.e(TAG, "onInteractionListener: "+otpTextView.getOtp());
            }

            @Override
            public void onOTPComplete(@NonNull String complete_otp) {
                //Toast.makeText(MPIN_SetupActivity.this, complete_otp, Toast.LENGTH_SHORT).show();
                if (MPIN.isEmpty()){
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
                }
            }
        });
    }

    private void btnClick() {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"1";
                otpTextView.setOTP(otp);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"2";
                otpTextView.setOTP(otp);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"3";
                otpTextView.setOTP(otp);
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"4";
                otpTextView.setOTP(otp);
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"5";
                otpTextView.setOTP(otp);
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"6";
                otpTextView.setOTP(otp);
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"7";
                otpTextView.setOTP(otp);
            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"8";
                otpTextView.setOTP(otp);
            }
        });
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"9";
                otpTextView.setOTP(otp);
            }
        });
        tv0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=otpTextView.getOtp()+"0";
                otpTextView.setOTP(otp);
            }
        });
        llBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!otpTextView.getOtp().isEmpty()){
                    clearOTP(otpTextView.getOtp());
                }
            }
        });
    }

    private void clearOTP(String otp) {
        String part = otp.substring(0, otp.length()-1);
        Log.e(TAG, "clearOTP: "+part);
        otpTextView.setOTP(part);
    }
}
