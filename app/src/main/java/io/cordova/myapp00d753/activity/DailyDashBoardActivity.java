package io.cordova.myapp00d753.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.attendance.AttendanceReportActivity;
import io.cordova.myapp00d753.utility.NetworkConnectionCheck;


public class DailyDashBoardActivity extends AppCompatActivity {
    LinearLayout llManage, llReport, llLog;
    LinearLayout llManageD, llReportD, llLogD;
    LinearLayout llManageD1, llReportD1, llLogD1;
    ImageView imgBack, imgHome;
    NetworkConnectionCheck connectionCheck;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_dash_board);
        initialize();
        onClick();
    }

    private void initialize() {
        connectionCheck = new NetworkConnectionCheck(DailyDashBoardActivity.this);
        llManage = (LinearLayout) findViewById(R.id.llManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);

        llManageD = (LinearLayout) findViewById(R.id.llManageD);
        llReportD = (LinearLayout) findViewById(R.id.llReportD);

        llManageD1 = (LinearLayout) findViewById(R.id.llManageD1);
        llReportD1 = (LinearLayout) findViewById(R.id.llReportD1);

        llLog = (LinearLayout) findViewById(R.id.llLog);
        llLogD = (LinearLayout) findViewById(R.id.llLogD);
        llLogD1 = (LinearLayout) findViewById(R.id.llLogD1);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    private void onClick() {
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 progressDialog.show();
                llManageD.setVisibility(View.GONE);
                llManageD1.setVisibility(View.VISIBLE);

                llReportD.setVisibility(View.VISIBLE);
                llReportD1.setVisibility(View.GONE);

                llLogD.setVisibility(View.VISIBLE);
                llLogD1.setVisibility(View.GONE);


                Intent intent = new Intent(DailyDashBoardActivity.this, VisitLocationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        llLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llReportD.setVisibility(View.VISIBLE);
                llReportD1.setVisibility(View.GONE);

                llManageD.setVisibility(View.VISIBLE);
                llManageD1.setVisibility(View.GONE);

                llLogD.setVisibility(View.GONE);
                llLogD1.setVisibility(View.VISIBLE);
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(DailyDashBoardActivity.this, NumberTourActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DailyDashBoardActivity.this, OfflineAttenReportActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });


        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llReportD.setVisibility(View.GONE);
                llReportD1.setVisibility(View.VISIBLE);

                llManageD.setVisibility(View.VISIBLE);
                llManageD1.setVisibility(View.GONE);

                llLogD.setVisibility(View.VISIBLE);
                llLogD1.setVisibility(View.GONE);


                Intent intent = new Intent(DailyDashBoardActivity.this, AttendanceReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyDashBoardActivity.this, EmployeeDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.dismiss();
    }
}
