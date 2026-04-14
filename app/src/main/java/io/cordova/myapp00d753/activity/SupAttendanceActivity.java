package io.cordova.myapp00d753.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.murugappa.AttendanceDashboardActivity;
import io.cordova.myapp00d753.adapter.ODOMeterApprovalAdapter;
import io.cordova.myapp00d753.bluedart.ODOmeterApprvalActivity;
import io.cordova.myapp00d753.utility.Pref;

public class SupAttendanceActivity extends AppCompatActivity {
    private static final String TAG = "SupAttendanceActivity";
    LinearLayout llAttandanceManage,llAttendanceReport,llApproval,llQR,llODOmeter,llAdjApproval;
    ImageView imgBack,imgHome;
    Pref pref;
    String NewLMSAccess="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_attendance);
        initialize();
        onClick();
    }

    private void initialize(){
        pref=new Pref(SupAttendanceActivity.this);
        if(getIntent() != null){
            NewLMSAccess = getIntent().getStringExtra("NewLMSAccess");
        }
        llAttandanceManage=(LinearLayout)findViewById(R.id.llAttandanceManage);
        llAdjApproval=findViewById(R.id.llAdjApproval);
        llAttendanceReport=(LinearLayout)findViewById(R.id.llAttendanceReport);
        llODOmeter=(LinearLayout)findViewById(R.id.llODOmeter);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        llQR=(LinearLayout)findViewById(R.id.llQR);
        if (pref.getEmpClintId().equals("AEMCLI0910000315")){
            llQR.setVisibility(View.VISIBLE);
        }else {
            llQR.setVisibility(View.GONE);
        }

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

    }

    private void onClick(){
        if (pref.getEmpClintId().equals("AEMCLI1810001410")){
            llODOmeter.setVisibility(View.VISIBLE);
        } else {
            llODOmeter.setVisibility(View.GONE);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SupAttendanceActivity.this,SuperVisiorDashBoardActivity.class);
                startActivity(intent);
                 finish();
            }
        });
        llAttandanceManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pref.getEmpClintId().equalsIgnoreCase("AEMCLI2410001867")){
                    Intent intent=new Intent(SupAttendanceActivity.this, AttendanceDashboardActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(SupAttendanceActivity.this,SupAttenManageActivity.class);
                    startActivity(intent);
                }


            }
        });

        llAttendanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this,SupAttenReportActivity.class);
                startActivity(intent);
            }
        });

        llQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this,QRGeneratorActivity.class);
                startActivity(intent);
            }
        });

        llODOmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SupAttendanceActivity.this, ODOmeterApprvalActivity.class);
                startActivity(intent);
            }
        });

        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NewLMSAccess.equals("1")) {
                    Intent intent = new Intent(SupAttendanceActivity.this, ITViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", pref.getRegApprovalURL());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SupAttendanceActivity.this, AttenApprovalActivity.class);
                    startActivity(intent);
                }
            }
        });


        llAdjApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NewLMSAccess.equals("1")) {
                    Intent intent = new Intent(SupAttendanceActivity.this, ITViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", pref.getAdjApprovalURL());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SupAttendanceActivity.this, AttenApprovalActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
