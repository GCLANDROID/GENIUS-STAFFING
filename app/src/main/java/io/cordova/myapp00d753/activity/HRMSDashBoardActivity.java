package io.cordova.myapp00d753.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.utility.Pref;

public class HRMSDashBoardActivity extends AppCompatActivity {
    LinearLayout llKYC, llDOC, llKYCD, llKYCD1, llDOCD, llDOCD1;
    TextView tvEmployeeName, tvLoginDateTime, tvGreeting;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrmsdash_board);
        initialize();
        onClick();
    }

    private void initialize() {
        pref = new Pref(HRMSDashBoardActivity.this);

        llKYC = (LinearLayout) findViewById(R.id.llKYC);
        llKYCD = (LinearLayout) findViewById(R.id.llKYCD);
        llKYCD1 = (LinearLayout) findViewById(R.id.llKYCD1);

        llDOC = (LinearLayout) findViewById(R.id.llDOC);
        llDOCD = (LinearLayout) findViewById(R.id.llDOCD);
        llDOCD1 = (LinearLayout) findViewById(R.id.llDOCD1);

        tvEmployeeName = (TextView) findViewById(R.id.tvEmployeeName);
        tvEmployeeName.setText(pref.getEmpName());

        tvLoginDateTime = (TextView) findViewById(R.id.tvLoginDateTime);
        tvLoginDateTime.setText(pref.getloginTime());

        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            tvGreeting.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            tvGreeting.setText("Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            tvGreeting.setText("Good Evening");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {

            tvGreeting.setText("Good Evening");
        }


    }

    private void onClick() {
        llKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llKYCD.setVisibility(View.GONE);
                llKYCD1.setVisibility(View.VISIBLE);
                llDOCD.setVisibility(View.VISIBLE);
                llDOCD1.setVisibility(View.GONE);
                Intent intent = new Intent(HRMSDashBoardActivity.this, KYCDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llDOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llKYCD.setVisibility(View.VISIBLE);
                llKYCD1.setVisibility(View.GONE);
                llDOCD.setVisibility(View.GONE);
                llDOCD1.setVisibility(View.VISIBLE);
            }
        });

    }
}